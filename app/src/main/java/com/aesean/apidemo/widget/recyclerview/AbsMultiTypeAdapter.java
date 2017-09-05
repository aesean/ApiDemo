package com.aesean.apidemo.widget.recyclerview;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
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
 * @version 1.1
 * @since 2017-08-05
 */
@SuppressWarnings({"WeakerAccess", "unused"})
abstract class AbsMultiTypeAdapter extends RecyclerView.Adapter<AbsViewHolder<?>> {
    protected final String TAG = this.getClass().getName();
    protected static final boolean DEBUG_MODE = BuildConfig.DEBUG;

    private static final int NOT_FOUND_VIEW_TYPE = 0;

    private boolean mCheckData = true;

    /**
     * BindMap记录所有数据类型对应的ViewHolder的类类型。
     * 所有数据类型最终都会到保存到BindMap中，如果是接口，将会在{@link #getItemViewType(int)}后，将实际类型保存到BindMap中。
     * 为了提高效率，这里没有使用Map<Class,Class>，数据类型会通过{@link #getKey(Class)}转换成int数值。
     * <p>
     * 记录所有数据类型对应的ViewHolderKey
     */
    private SparseIntArray mBindMap = new SparseIntArray();
    /**
     * 记录接口对应BindMap中的key。
     */
    private Map<Class<?>, Integer> mInterfaceCacheMap = new HashMap<>();
    /**
     * 保存所有静态ViewHolder，主要是需要通过hashCode逆向获取ViewHolder
     */
    private SparseArray<Class<? extends AbsViewHolder<?>>> mViewTypeMap = new SparseArray<>();
    /**
     * 保存所有动态ViewHolder，主要是需要通过hashCode逆向获取ViewHolder
     */
    private SparseArray<ViewHolderCreator<?, ? extends AbsViewHolder<?>>> mViewCreator = new SparseArray<>();

    protected AbsMultiTypeAdapter() {
    }

    /**
     * 本质还是取HashCode，这里主要是为了防止类复写HashCode，引发无法准确定位数据类型。
     *
     * @param clazz 类类型
     * @return HashCode
     */
    private static int getKey(Class<?> clazz) {
        return System.identityHashCode(clazz);
    }

    /**
     * 注册数据对应的ViewHolder类型。
     *
     * @param dataType 数据的类类型。这里允许使用接口，当这里是接口的时候，那么添加进来的数据类型只要是接口的实现类，那么将使用对应的ViewHolder。
     *                 如果某个数据的类类型，注册了ViewHolder，同时这个数据也是一个接口的实现类，对应接口也注册了ViewHolder，那么会优先使用这
     *                 个数据的类类型注册的ViewHolder，不会使用对应接口注册的ViewHolder。
     *                 注意，如果实际添加的数据对应了多个已经注册了对应ViewHolder的接口，那么将随机匹配ViewHolder，数据类型请不要同时实现多个注册接口。
     *                 暂时不支持抽象数据类型的注册。
     * @param viewType 对应的ViewHolder类类型。
     * @param <T>      范型，静态检查数据类型和ViewHolder是对应的。
     */
    public <T> void register(@NonNull Class<? extends T> dataType, @NonNull Class<? extends AbsViewHolder<T>> viewType) {
        int dataTypeKey = getKey(dataType);
        int viewTypeKey = getKey(viewType);
        mViewTypeMap.put(viewTypeKey, viewType);
        if (dataType.isInterface()) {
            // 如果是接口，那么保存接口类类型和对应的ViewHolder的key。
            mInterfaceCacheMap.put(dataType, viewTypeKey);
        } else {
            // 不是接口，正常注册。
            mBindMap.put(dataTypeKey, viewTypeKey);
        }
    }

    /**
     * 注册数据对应的ViewHolder类型。
     *
     * @param dataType 数据的类类型。这里允许使用接口，当这里是接口的时候，那么添加进来的数据类型只要是接口的实现类，那么将使用对应的ViewHolder。
     *                 如果某个数据的类类型，注册了ViewHolder，同时这个数据也是一个接口的实现类，对应接口也注册了ViewHolder，那么会优先使用这
     *                 个数据的类类型注册的ViewHolder，不会使用对应接口注册的ViewHolder。
     *                 注意，如果实际添加的数据对应了多个已经注册了对应ViewHolder的接口，那么将随机匹配ViewHolder，数据类型请不要同时实现多个注册接口。
     *                 暂时不支持抽象数据类型的注册。
     * @param creator  ViewHolder构造器。通常用来注册回调，或者其他一些需要在ViewHolder创建时候做的初始化工作。
     * @param <M>      数据类型，范型，静态检查数据类型和ViewHolder是对应的。
     * @param <N>      ViewHolder类型，范型，静态检查数据类型和ViewHolder是对应的。
     */
    public <M, N extends AbsViewHolder<M>> void register(@NonNull Class<M> dataType, @NonNull ViewHolderCreator<M, N> creator) {
        int dataTypeKey = getKey(dataType);
        int viewTypeKey = getKey(creator.getViewHolderClass());
        mViewCreator.put(viewTypeKey, creator);
        if (dataType.isInterface()) {
            // 如果是接口，那么保存接口类类型和对应的ViewCreator的key。
            mInterfaceCacheMap.put(dataType, viewTypeKey);
        } else {
            // 不是接口，正常注册。
            mBindMap.put(dataTypeKey, viewTypeKey);
        }
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
        try {
            getItemViewClass(object);
        } catch (ViewTypeNotFoundException e) {
            throw new ViewTypeNotFoundException("当前对象没有在Adapter中注册，object = " + object.toString());
        }
    }

    @Override
    public final AbsViewHolder<?> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 优先检查ViewCreator
        ViewHolderCreator<?, ? extends AbsViewHolder<?>> viewHolderCreator = mViewCreator.get(viewType);
        if (viewHolderCreator != null) {
            return viewHolderCreator.create(parent);
        }
        // 检查ViewTypeMap
        Class<? extends AbsViewHolder<?>> viewClass = mViewTypeMap.get(viewType);
        if (viewClass == null) {
            throw new ViewTypeNotFoundException("当前数据类型没有注册对应的ViewHolder类型，ClassType = " + viewType);
        }
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

    private Class<? extends AbsViewHolder<?>> getItemViewClass(Object o) throws ViewTypeNotFoundException {
        Class<?> clazz = o.getClass();
        // 拿到数据类型对应的key
        final int dataKey = getKey(clazz);
        // 优先检查ViewCreator
        ViewHolderCreator<?, ? extends AbsViewHolder<?>> viewHolderCreator = mViewCreator.get(dataKey);
        if (viewHolderCreator != null) {
            return viewHolderCreator.getViewHolderClass();
        }
        // 尝试从BindMap中取出ViewHolder
        int viewTypeKey = mBindMap.get(dataKey, NOT_FOUND_VIEW_TYPE);
        if (viewTypeKey == NOT_FOUND_VIEW_TYPE) {
            // 允许数据类型实现ViewType接口来定义ViewHolder类类型。
            if (o instanceof ViewType) {
                Class<? extends AbsViewHolder<?>> viewType = ((ViewType) o).getViewType();
                int tempKey = getKey(viewType);
                // 注册到BindMap，避免多次遍历
                mBindMap.put(dataKey, tempKey);
                mViewTypeMap.put(tempKey, viewType);
                return viewType;
            }
            // 检查是否为某个注册接口的实现类。
            for (Map.Entry<Class<?>, Integer> entry : mInterfaceCacheMap.entrySet()) {
                Class<?> tempClass = entry.getKey();
                if (tempClass.isAssignableFrom(clazz)) {
                    Integer viewHolderKey = entry.getValue();
                    mBindMap.put(dataKey, viewHolderKey);
                    return findViewHolderClassType(viewTypeKey);
                }
            }
            // 没有找到数据类型对应的ViewHolder抛出异常。
            throw new ViewTypeNotFoundException("当前数据类型没有注册对应的ViewHolder类型，ClassType = " + clazz.toString());
        } else {
            return findViewHolderClassType(viewTypeKey);
        }
    }

    private Class<? extends AbsViewHolder<?>> findViewHolderClassType(int viewTypeKey) {
        ViewHolderCreator<?, ? extends AbsViewHolder<?>> creator = mViewCreator.get(viewTypeKey);
        if (creator != null) {
            return creator.getViewHolderClass();
        }
        return mViewTypeMap.get(viewTypeKey);
    }

    protected Class<? extends AbsViewHolder<?>> getItemViewClassByPosition(int position) throws ViewTypeNotFoundException {
        Object o = getData(position);
        return getItemViewClass(o);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getData(position);
        Class<?> clazz = o.getClass();
        // 拿到数据类型对应的key
        final int dataKey = getKey(clazz);
        // 优先尝试从BindMap中取出ViewHolder
        final int viewTypeKey = mBindMap.get(dataKey, NOT_FOUND_VIEW_TYPE);
        if (viewTypeKey == NOT_FOUND_VIEW_TYPE) {
            // 允许数据类型实现ViewType接口来定义ViewHolder类类型。
            if (o instanceof ViewType) {
                Class<? extends AbsViewHolder<?>> viewType = ((ViewType) o).getViewType();
                int tempKey = getKey(viewType);
                // 注册到BindMap，避免多次遍历
                mViewTypeMap.put(tempKey, viewType);
                mBindMap.put(dataKey, tempKey);
                return tempKey;
            }
            // 检查是否为某个注册接口的实现类。
            for (Map.Entry<Class<?>, Integer> entry : mInterfaceCacheMap.entrySet()) {
                Class<?> tempClass = entry.getKey();
                if (tempClass.isAssignableFrom(clazz)) {
                    Integer viewHolderKey = entry.getValue();
                    mBindMap.put(dataKey, viewHolderKey);
                    return viewHolderKey;
                }
            }
            // 没有找到数据类型对应的ViewHolder抛出异常。
            throw new ViewTypeNotFoundException("当前数据类型没有注册对应的ViewHolder类型，ClassType = " + clazz.toString());
        } else {
            return viewTypeKey;
        }
    }

    public static class ViewTypeNotFoundException extends RuntimeException {
        public ViewTypeNotFoundException() {
        }

        public ViewTypeNotFoundException(String message) {
            super(message);
        }

        public ViewTypeNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public ViewTypeNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
