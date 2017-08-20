package com.aesean.apidemo.widget.recyclerview;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.aesean.apidemo.BuildConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbsMultiTypeAdapter
 * 抽象多类型Adapter，支持多类型绑定，没有实现数据保存，可以派生子类实现各种不同数据类型。
 *
 * @author xl
 * @since 2017-08-05
 */
@SuppressWarnings({"WeakerAccess", "unused"})
abstract class AbsMultiTypeAdapter extends RecyclerView.Adapter<AbsViewHolder<?>> {
    protected final String TAG = this.getClass().getName();
    protected static final boolean DEBUG_MODE = BuildConfig.DEBUG;

    private boolean mCheckData = true;

    private SparseArray<Class<? extends AbsViewHolder<?>>> mBindMap = new SparseArray<>();
    private Map<Class<?>, Integer> mInterfaceCacheMap = new HashMap<>();

    protected AbsMultiTypeAdapter() {
    }

    /**
     * 本质还是取HashCode，这里主要是为了防止类复写HashCode，引发无法准确定位数据类型。
     *
     * @param clazz 类类型
     * @return HashCode
     */
    public static int getViewType(Class<?> clazz) {
        return System.identityHashCode(clazz);
    }

    public <T> void register(Class<? extends T> dataType, Class<? extends AbsViewHolder<T>> viewType) {
        int type = getViewType(dataType);
        if (dataType.isInterface()) {
            mInterfaceCacheMap.put(dataType, type);
        }
        mBindMap.put(type, viewType);
    }

    protected final <T> void checkAllData(List<T> list) {
        if (!mCheckData) {
            return;
        }
        for (T o : list) {
            check(o);
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T> void checkData(T... objects) {
        if (!mCheckData) {
            return;
        }
        for (T object : objects) {
            check(object);
        }
    }

    private <T> void check(T object) {
        if (!mCheckData) {
            return;
        }
        Class<?> clazz = object.getClass();
        int index = mBindMap.indexOfKey(getViewType(clazz));
        if (index < 0) {
            for (Map.Entry<Class<?>, Integer> entry : mInterfaceCacheMap.entrySet()) {
                Class<?> keyClass = entry.getKey();
                if (keyClass.isAssignableFrom(clazz)) {
                    return;
                }
            }
            throw new RuntimeException("当前对象没有在Adapter中注册，object = " + clazz.toString());
        }
    }

    private Class<? extends AbsViewHolder<?>> getViewClass(int viewType) {
        Class<? extends AbsViewHolder<?>> clazz = mBindMap.get(viewType);
        if (clazz == null) {
            throw new RuntimeException("当前数据类型没有注册对应的ViewHolder类型，ClassType = " + viewType);
        }
        return clazz;
    }

    @Override
    public final AbsViewHolder<?> onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends AbsViewHolder<?>> viewClass = getViewClass(viewType);
        try {
            Constructor<? extends AbsViewHolder<?>> constructor = viewClass.getDeclaredConstructor(ViewGroup.class);
            // if (!constructor.isAccessible()) {
            //     constructor.setAccessible(true);
            // }
            AbsViewHolder<?> viewHolder = constructor.newInstance(parent);
            viewHolder.attach(this);
            return viewHolder;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("必须实现参数为ViewGroup的构造器。Class = " + viewClass.toString());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("参数为ViewGroup的构造器不可访问。Class = " + viewClass.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("实例化ViewHolder异常，Class = " + viewClass.toString());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("ViewHolder调用异常，Class = " + viewClass.toString());
        }
    }

    @CallSuper
    @Override
    public void onBindViewHolder(AbsViewHolder<?> holder, int position) {
        Object o = getData(position);
        holder.bindDataSafe(o);
    }

    public void addAllItem(Object[] objects) {
        checkData(objects);
        addAllItem(getItemCount(), Arrays.asList(objects));
    }

    public <T> void addAllItem(List<T> list) {
        checkAllData(list);
        int position = getItemCount();
        addAllData(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void addItem(Object object) {
        checkData(object);
        int position = getItemCount();
        addData(position, object);
        notifyItemInserted(position);
    }

    protected <T> void addData(int position, T[] objects) {
        addData(position, Arrays.asList(objects));
    }

    public <T> void addAllItem(int position, List<T> list) {
        checkAllData(list);
        addAllData(position, list);
        notifyItemRangeInserted(position, list.size());
    }

    protected abstract <T> void addAllData(int position, List<T> list);

    public void addItem(int position, Object object) {
        checkData(object);
        addData(position, object);
        notifyItemInserted(position);
    }

    protected abstract void addData(int position, Object object);

    public void removeItem(Object object) {
        removeItem(getPosition(object));
    }

    public void removeItem(int position) {
        removeData(position);
        notifyItemRemoved(position);
    }

    protected abstract void removeData(int position);

    protected abstract void updateData(int position, Object object);

    public void updateItem(int position) {
        notifyItemChanged(position);
    }

    public void updateItem(int position, Object object) {
        checkData(object);
        updateData(position, object);
        notifyItemChanged(position);
    }

    public void updateItem(Object object) {
        int position = getPosition(object);
        updateItem(position, object);
    }

    private static <T> T getData(ArrayList<ArrayList<T>> source, int index) {
        int offset = 0;
        for (ArrayList<T> list : source) {
            int max = offset + list.size();
            if (index < max) {
                return list.get(index - offset);
            }
            offset = max;
        }
        throw new IndexOutOfBoundsException("index = " + index + ", maxSize = " + offset);
    }

    private static <T> int indexOfData(ArrayList<ArrayList<T>> source, T t) {
        int index = 0;
        for (ArrayList<T> list : source) {
            int offset = list.indexOf(t);
            if (offset > 0) {
                return index + offset;
            }
            index += list.size();
        }
        return -1;
    }

    public abstract int getPosition(Object object);

    @NonNull
    public abstract Object getData(int position);

    @Override
    public int getItemViewType(int position) {
        Object o = getData(position);
        if (o instanceof ViewType) {
            Class<? extends AbsViewHolder<?>> viewType = ((ViewType) o).getViewType();
            return getViewType(viewType);
        }
        Class<?> clazz = o.getClass();
        int type = getViewType(clazz);
        Class viewType = mBindMap.get(type);
        if (viewType == null) {
            for (Map.Entry<Class<?>, Integer> entry : mInterfaceCacheMap.entrySet()) {
                Class<?> key = entry.getKey();
                if (key.isAssignableFrom(clazz)) {
                    Integer value = entry.getValue();
                    int key1 = getViewType(key);
                    mBindMap.put(key1, mBindMap.get(value));
                    return key1;
                }
            }
            throw new RuntimeException("当前数据类型没有注册对应的ViewHolder类型，ClassType = " + clazz.toString());
        }

        return type;
    }
}
