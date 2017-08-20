package com.aesean.apidemo.widget.recyclerview;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * MultiTypeAdapter
 *
 * @author xl
 * @version 1.0
 * @since 2017-08-04
 */
public class MultiTypeAdapterImpl extends AbsMultiTypeAdapter {

    private ArrayList<ArrayList<Object>> mDataSource;

    public MultiTypeAdapterImpl() {
        super();
        mDataSource = new ArrayList<>();
    }

    public <T> void addAllItemByType(int type, List<T> list) {
        checkSize(mDataSource, type + 1);
        addAllItem(type, mDataSource.get(type).size(), list);
    }

    public void addItemByType(int type, Object data) {
        checkSize(mDataSource, type + 1);
        addItem(type, mDataSource.get(type).size(), data);
    }

    public <T> void addAllItem(int type, int offset, List<T> list) {
        checkAllData(list);
        int position = addAllData(mDataSource, type, offset, list);
        notifyItemRangeInserted(position, list.size());
    }

    public void addItem(int type, int offset, Object data) {
        checkData(data);
        int position = addData(mDataSource, type, offset, data);
        notifyItemInserted(position);
    }

    public void removeItem(int type, int offset) {
        int position = removeData(mDataSource, type, offset);
        notifyItemInserted(position);
    }

    public void removeAllItemByType(int type) {
        checkSize(mDataSource, type + 1);
        int count = mDataSource.get(type).size();
        if (count == 0) {
            return;
        }
        int position = positionOf(mDataSource, type, 0);
        removeAllData(mDataSource, type);
        notifyItemRangeRemoved(position, count);
    }

    @Override
    protected <T> void addAllData(int position, List<T> list) {
        addAllData(mDataSource, position, list, true);
    }

    @Override
    protected void addData(int position, Object object) {
        addData(mDataSource, position, object, true);
    }

    public List<Object> getAllData(int type) {
        ArrayList<Object> list = mDataSource.get(type);
        // mDataSource数据源的变化，肯定要同步notifyDataSet。
        // 这里禁止外部拿到mDataSource的引用。所以这里会新返回一个List对象。
        List<Object> result = new ArrayList<>();
        result.addAll(list);
        return result;
    }

    @NonNull
    public Object getData(int type, int offset) {
        return mDataSource.get(type).get(offset);
    }

    @Override
    protected void removeData(int position) {
        removeData(mDataSource, position);
    }

    @Override
    protected void updateData(int position, Object object) {
        updateData(mDataSource, position, object);
    }

    @Override
    public int getPosition(Object object) {
        return positionOfData(mDataSource, object);
    }

    @NonNull
    @Override
    public Object getData(int position) {
        return getData(mDataSource, position);
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    private int getSize() {
        int size = 0;
        for (ArrayList<Object> objects : mDataSource) {
            size += objects.size();
        }
        return size;
    }

    /**
     * 确保最外层List的最小Size。
     * offset不允许跨index add，但是type是允许跨index添加的。
     *
     * @param list list
     * @param size 最小Size
     */
    private static void checkSize(ArrayList<ArrayList<Object>> list, int size) {
        while (list.size() < size) {
            list.add(list.size(), new ArrayList<>());
        }
    }

    /**
     * 添加数据
     *
     * @param source   数据源
     * @param position 需要添加的位置
     * @param object   需要添加的对象
     * @param append   true 追加到前一个List尾部，false 插入到下一个List头部。
     *                 如果插入的数据刚好在分界出则需要指定如何处理数据。比如source大小为2，每个子list长度为3，
     *                 当插入position为3的时候，如果append为true则会插入到source.get(0)的最后一个，
     *                 如果append为false则会插入到source.get(1)的第一个。
     */
    private static void addData(ArrayList<ArrayList<Object>> source, int position, Object object, boolean append) {
        int offset = 0;
        checkSize(source, 1);
        for (ArrayList<Object> list : source) {
            int size = list.size();
            int temp = offset + size;
            if ((position < temp) || (position == temp && append)) {
                list.add(position - offset, object);
                return;
            }
            offset += size;
        }
    }

    private static <T> int addAllData(ArrayList<ArrayList<Object>> source, int index, int offset, List<T> list) {
        checkSize(source, index + 1);
        int position = positionOf(source, index, offset);
        source.get(index).addAll(offset, list);
        return position;
    }

    private static <T> void addAllData(ArrayList<ArrayList<Object>> source, int position, List<T> object, boolean append) {
        int offset = 0;
        checkSize(source, 1);
        for (ArrayList<Object> list : source) {
            int size = list.size();
            int temp = offset + size;
            if ((position < temp) || (position == temp && append)) {
                list.addAll(position - offset, object);
                return;
            }
            offset += size;
        }
    }

    private static int addData(ArrayList<ArrayList<Object>> source, int index, int offset, Object object) {
        checkSize(source, index + 1);
        source.get(index).add(offset, object);
        int position = 0;
        for (int i = 0; i < index; i++) {
            position += source.get(i).size();
        }
        position += offset;
        return position;
    }


    private static void updateData(ArrayList<ArrayList<Object>> source, int position, Object object) {
        int offset = 0;
        for (ArrayList<Object> list : source) {
            int size = list.size();
            int temp = offset + size;
            if (position < temp) {
                list.remove(position - offset);
                list.add(position - offset, object);
                return;
            }
            offset += size;
        }
    }

    private static void removeAllData(ArrayList<ArrayList<Object>> source, int index) {
        source.get(index).clear();
    }

    private static int removeData(ArrayList<ArrayList<Object>> source, int index, int offset) {
        int position = positionOf(source, index, offset);
        source.get(index).remove(offset);
        return position;
    }

    private static void removeData(ArrayList<ArrayList<Object>> source, int position) {
        int offset = 0;
        for (ArrayList<Object> list : source) {
            int size = list.size();
            int temp = offset + size;
            if (position < temp) {
                list.remove(position - offset);
                return;
            }
            offset += size;
        }
    }

    private static Object getData(ArrayList<ArrayList<Object>> source, int position) {
        int offset = 0;
        for (ArrayList<Object> list : source) {
            int max = offset + list.size();
            if (position < max) {
                return list.get(position - offset);
            }
            offset = max;
        }
        throw new IndexOutOfBoundsException("index = " + position + ", maxSize = " + offset);
    }

    private static int positionOf(ArrayList<ArrayList<Object>> source, int index, int offset) {
        int position = 0;
        for (int i = 0; i < index; i++) {
            position += source.get(i).size();
        }
        return position + offset;
    }

    private static int positionOfData(ArrayList<ArrayList<Object>> source, Object t) {
        int position = 0;
        for (ArrayList<Object> list : source) {
            int offset = list.indexOf(t);
            if (offset >= 0) {
                return position + offset;
            }
            position += list.size();
        }
        return -1;
    }
}
