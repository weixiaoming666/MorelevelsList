package com.example.morelevelslist;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class LevelsAdapter2 extends RecyclerView.Adapter  implements ItemTouchHelperAdapter {
    List<NodeEntity> datas = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    private LinkedHashMap<String, NodeEntity> map = new LinkedHashMap();//顺序的map来处理数据的变化


    public LevelsAdapter2(List<NodeEntity> datas, Context context) {
//        -通過LinkedHashMap持有数据并且通过和 List<NodeEntity> datas的交互保持数据的实时更新
       this.datas = datas;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
//            节点
            case -1:
                View view = layoutInflater.inflate(R.layout.item_node, null);
                holder = new LevelsAdapter2.ViewHolderNode(view);
                break;
//               子节点样式1
            case 1:
                View view1 = layoutInflater.inflate(R.layout.item_one, null);
                holder = new LevelsAdapter2.ViewHolderItemOne(view1);
                break;
        }
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case -1:
                initViewNode((LevelsAdapter2.ViewHolderNode) holder, datas.get(position));
                break;
            case 1:
                initViewOne((LevelsAdapter2.ViewHolderItemOne) holder, datas.get(position), position);
                break;
        }
    }

    public LevelsAdapter2(List<NodeEntity> datas) {
        this.datas = datas;
    }

    /**
     * 显示  显示样式1
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViewOne(LevelsAdapter2.ViewHolderItemOne holder, NodeEntity nodeEntity, int position) {
        holder.tvTitle.setText(nodeEntity.title);
        holder.etInput.setHint("请输入：" + nodeEntity.sequence);
        holder.itemView.setBackgroundColor(context.getColor(R.color.red_DD0404));
        LevelsAdapter2.MyTextWatcher myTextWatcher;
        if (holder.etInput.getTag() == null) {
            myTextWatcher = new LevelsAdapter2.MyTextWatcher(position);
            holder.etInput.setTag(myTextWatcher);
        } else {
            myTextWatcher = (LevelsAdapter2.MyTextWatcher) holder.etInput.getTag();
            myTextWatcher.upIndex(position);
        }
        holder.etInput.addTextChangedListener(myTextWatcher);
        isSetEt = true;
        holder.etInput.setText(nodeEntity.content);
        isSetEt = false;
    }

    /**
     * 显示节点内容
     */
    private boolean setChecked = true;//设置数据时不走监听

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViewNode(LevelsAdapter2.ViewHolderNode holder, NodeEntity nodeEntity) {
        String[] length = nodeEntity.sequence.split("\\.");
        switch (length.length) {
            case 1:
                holder.itemView.setBackgroundColor(context.getColor(R.color.white));
                showTree(holder, length.length);
                break;
            case 2:
                holder.itemView.setBackgroundColor(context.getColor(R.color.teal_200));
                showTree(holder, length.length);
                break;

            case 3:
                showTree(holder, length.length);
                holder.itemView.setBackgroundColor(context.getColor(R.color.purple_200));
                break;

            case 4:
                showTree(holder, length.length);
                holder.itemView.setBackgroundColor(context.getColor(R.color.teal_700));
                break;


        }
        holder.tvTitle.setText(nodeEntity.title);
        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (setChecked) return;
                if (isChecked) {
                    nodeEntity.isOpen = true;
                    for (NodeEntity entity : map.values()) {
                        if (entity.parentId.equals(nodeEntity.sequence)) {
                            entity.isShow = true;
                        }
                    }
                } else {
                    nodeEntity.isOpen = false;
                    for (NodeEntity entity : map.values()) {
                        if (entity.parentId.startsWith(nodeEntity.sequence)) {
                            entity.isShow = false;
//                            这里全部关掉 open 也是
                            entity.isOpen = false;
                        }
                    }
                }
                dataChanged();
            }
        });
        setChecked = true;
        holder.cb.setChecked(nodeEntity.isOpen);
        setChecked = false;
    }
//     这里其实处理的话直接用根据具体的ui个和层级使用横向滑动的RecyclerView或者listview去处理更简洁（节点层级很多的清空下）

    private void showTree(LevelsAdapter2.ViewHolderNode holder, int length) {
        switch (length) {
            case 1:
                holder.iv1.setVisibility(View.VISIBLE);
                holder.iv2.setVisibility(View.GONE);
                holder.iv3.setVisibility(View.GONE);
                holder.iv4.setVisibility(View.GONE);
                break;
            case 2:
                holder.iv1.setVisibility(View.INVISIBLE);
                holder.iv2.setVisibility(View.VISIBLE);
                holder.iv3.setVisibility(View.GONE);
                holder.iv4.setVisibility(View.GONE);
                break;
            case 3:
                holder.iv1.setVisibility(View.INVISIBLE);
                holder.iv2.setVisibility(View.INVISIBLE);
                holder.iv3.setVisibility(View.VISIBLE);
                holder.iv4.setVisibility(View.GONE);
                break;
            case 4:
                holder.iv1.setVisibility(View.INVISIBLE);
                holder.iv2.setVisibility(View.INVISIBLE);
                holder.iv3.setVisibility(View.INVISIBLE);
                holder.iv4.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 变化数据  修正view的现实
     */
    private void dataChanged() {
//        现在的位置
        datas.clear();
        for (NodeEntity nodeEntity : map.values()) {
            if (nodeEntity.isShow) {
                nodeEntity.position = datas.size();
                datas.add(nodeEntity);

            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).viewType;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 获取提交数据
     *
     * @param rlv
     */
    public List<NodeEntity> getDatas(RecyclerView rlv) {
        List<NodeEntity> datasTem = new ArrayList<>();//这里不能直接用datas因为他实际在被持有且使用
        for (NodeEntity nodeEntity : map.values()) {
            if (nodeEntity.viewType == 1 && TextUtils.isEmpty(nodeEntity.content)) {
                Toast.makeText(context, nodeEntity.title + "内容必须填写", Toast.LENGTH_SHORT).show();
                jumpToPosition(nodeEntity, rlv);
                return null;
            }
            datasTem.add(nodeEntity);
        }
        datas.clear();
        datas.addAll(datasTem);
        return datas;

    }

    /**
     * 跳转到待处理的位置（显示中或者未显示）
     */
    private void jumpToPosition(NodeEntity nodeEntity, RecyclerView rlv) {
        if (nodeEntity.isShow) {
            rlv.scrollToPosition(nodeEntity.position);
        } else {
            nodeEntity.isShow = true;
            for (NodeEntity entity : map.values()) {
                if (nodeEntity.sequence.startsWith(entity.sequence) || (nodeEntity.sequence.startsWith(entity.parentId))) {
                    entity.isShow = true;
                    entity.isOpen = true;
                }
            }
            dataChanged();
            rlv.scrollToPosition(nodeEntity.position);
        }

    }

    //    滑动处理
    @Override
    public void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();//来自位置
        int toPosition = target.getAdapterPosition();
        if (datas.get(fromPosition).parentId.equals("-1")){//分组
            if (datas.get(toPosition).parentId.equals("-1")){//分组只能跟分组进行位置互换
                Collections.swap(datas, fromPosition, toPosition);
                notifyItemMoved(fromPosition, toPosition);

                //                需要处理的就是这个节点之间的数据互换了；

                List<NodeEntity> toPositionDatas = new ArrayList<>();//点击的分组下的数据
                List<NodeEntity> fromPositionDatas = new ArrayList<>();//获取的分组下的数据
                NodeEntity toPositionNode = datas.get(fromPosition);//点击分组的数据
                NodeEntity fromPositionsNode = datas.get(toPosition);//交换分组的数据
                Log.d("排序后的数据", datas.toString());
                for (int i = 0; i < datas.size(); i++) {
//                    移动后的位置
                    if (datas.get(i).parentId.equals(toPositionNode.sequence)){
                        toPositionDatas.add(datas.get(i));
                        datas.remove(datas.get(i));
                        i--;
                    }
                    if (datas.get(i).parentId.equals(fromPositionsNode.sequence)){
                        fromPositionDatas.add(datas.get(i));
                        datas.remove(datas.get(i));
                        i--;
                    }
                }
                datas.addAll(datas.indexOf(toPositionNode)+1,toPositionDatas);
                datas.addAll(datas.indexOf(fromPositionsNode)+1,fromPositionDatas);
                Log.d("排序后的数据2", datas.toString());
                if (fromPosition>toPosition){
                    notifyItemRangeChanged(toPosition+1,toPositionDatas.size()+fromPositionDatas.size()+1);
                }else {
                    notifyItemRangeChanged(fromPosition,toPositionDatas.size()+fromPositionDatas.size()+1);

                }
                onItemClear(source);
            }

        }else {//单体
            if (fromPosition < datas.size() && toPosition < datas.size()&&toPosition!=0) {
                //交换数据位置
                Collections.swap(datas, fromPosition, toPosition);
                if (datas.get(toPosition-1).parentId.equals("-1")){
                    datas.get(toPosition).parentId = datas.get(toPosition-1).sequence;//官服父id的获取;
                }else {
                    datas.get(toPosition).parentId = datas.get(toPosition-1).parentId;//官服父id的获取;
                }

                //刷新位置交换
                notifyItemMoved(fromPosition, toPosition);
            }
            onItemClear(source);
        }



        //移动过程中移除view的放大效果
        Log.d("排序后的数据", datas.toString());
        Log.d("排序后的数据：", "fromPosition:" +fromPosition+"****TO***"+"toPosition:"+toPosition);

    }

    @Override
    public void onItemDissmiss(RecyclerView.ViewHolder source) {
        int position = source.getAdapterPosition();
        datas.remove(position); //移除数据
        notifyItemRemoved(position);//刷新数据移除
        Log.d("排序后的数据：", "onItemDissmiss");
    }

    @Override
    public void onItemSelect(RecyclerView.ViewHolder source, int actionState) {
        //当拖拽选中时放大选中的view
        if (actionState == 0){
//            source.itemView.setScaleX(1.0f);
//            source.itemView.setScaleY(1.0f);
        }else if (actionState == 2){
            source.itemView.setScaleX(0.9f);
            source.itemView.setScaleY(0.9f);
        }

    }

    @Override
    public void onItemClear(RecyclerView.ViewHolder source) {
//拖拽结束后恢复view的状态
        source.itemView.setScaleX(1.0f);
            source.itemView.setScaleY(1.0f);
        Log.d("排序后的数据：", "清除");

    }

    @Override
    public boolean canDropOver(int fromPosition, int toPosition) {
        if (datas.get(fromPosition).parentId.equals("-1")){//分组
            if (datas.get(toPosition).parentId.equals("-1")){//分组只能跟分组进行位置互换
                return true;

            }
        }else {
            if (fromPosition < datas.size() && toPosition < datas.size()&&toPosition!=0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 打开节点
     */
    class ViewHolderNode extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTitle;
        private CheckBox cb;
        private ImageView iv1;
        private ImageView iv2;
        private ImageView iv3;
        private ImageView iv4;

        public ViewHolderNode(@NonNull View itemView) {
            super(itemView);
            tvTitle = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
            cb = (CheckBox) itemView.findViewById(R.id.cb);
//            可以用列表替代的
            iv1 = (ImageView) itemView.findViewById(R.id.iv1);
            iv2 = (ImageView) itemView.findViewById(R.id.iv2);
            iv3 = (ImageView) itemView.findViewById(R.id.iv3);
            iv4 = (ImageView) itemView.findViewById(R.id.iv4);
            iv1.setVisibility(View.GONE);
            iv2.setVisibility(View.GONE);
            iv3.setVisibility(View.GONE);
            iv4.setVisibility(View.GONE);


        }
    }

    /**
     * 子节点内容样式1
     */
    class ViewHolderItemOne extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTitle;
        private EditText etInput;

        public ViewHolderItemOne(@NonNull View itemView) {
            super(itemView);
            tvTitle = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
            etInput = (EditText) itemView.findViewById(R.id.et_input);

        }
    }

    /**
     * 自定义的字体监听  防止et数据混乱
     */
    private boolean isSetEt = false;

    public class MyTextWatcher implements TextWatcher {
        private int index = -1;  //位置
        private int type = -1;  //赋值类型

        public MyTextWatcher(int position) {
            index = position;
        }

        public void upIndex(int position) {
            index = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isSetEt) return;
            datas.get(index).content = s.toString();
        }
    }

}
