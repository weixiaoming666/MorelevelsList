package com.example.morelevelslist;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LevelsAdapter extends RecyclerView.Adapter {
    List<NodeEntity> datas = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context;
    private LinkedHashMap<String, NodeEntity> map = new LinkedHashMap();//顺序的map来处理数据的变化


    public LevelsAdapter(List<NodeEntity> datas, Context context) {
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isShow){
                datas.get(i).position =  this.datas.size();
                this.datas.add(datas.get(i));
            }
            map.put(datas.get(i).sequence, datas.get(i));

        }
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
                holder = new ViewHolderNode(view);
                break;
//               子节点样式1
            case 1:
                View view1 = layoutInflater.inflate(R.layout.item_one, null);
                holder = new ViewHolderItemOne(view1);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case -1:
                initViewNode((ViewHolderNode) holder, datas.get(position));
                break;
            case 1:
                initViewOne((ViewHolderItemOne) holder, datas.get(position),position);
                break;
        }
    }

    public LevelsAdapter(List<NodeEntity> datas) {
        this.datas = datas;
    }

    /**
    * 显示  显示样式1
    * */
    private void initViewOne(ViewHolderItemOne holder, NodeEntity nodeEntity, int position) {
        holder.tvTitle.setText(nodeEntity.title);
        holder.etInput.setHint("请输入："+nodeEntity.sequence);
        holder.itemView.setBackgroundColor(context.getColor(R.color.red_DD0404));
        MyTextWatcher myTextWatcher;
        if (  holder.etInput.getTag() == null){
             myTextWatcher   = new MyTextWatcher(position);
            holder.etInput.setTag(myTextWatcher);
        }else {
            myTextWatcher = (MyTextWatcher) holder.etInput.getTag();
            myTextWatcher.upIndex(position);
        }
        holder.etInput.addTextChangedListener(myTextWatcher);
        isSetEt = true;
        holder.etInput.setText(nodeEntity.content);
        isSetEt = false;
    }

    /**
    * 显示节点内容
    * */
    private boolean setChecked = true;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViewNode(ViewHolderNode holder, NodeEntity nodeEntity) {
        String[] length =  nodeEntity.sequence.split("\\.");
        switch (length.length){
            case 1:
                holder.itemView.setBackgroundColor(context.getColor(R.color.white));
                showTree(holder,length.length);
                break;
            case 2:
                holder.itemView.setBackgroundColor(context.getColor(R.color.teal_200));
                showTree(holder,length.length);
                break;

            case 3:
                showTree(holder,length.length);
                holder.itemView.setBackgroundColor(context.getColor(R.color.purple_200));
                break;

            case 4:
                showTree(holder,length.length);
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

    private void showTree(ViewHolderNode holder, int length) {
        switch (length){
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
 * @param rlv*/
    public  List<NodeEntity> getDatas(RecyclerView rlv) {
        List<NodeEntity> datasTem = new ArrayList<>();
        for (NodeEntity nodeEntity: map.values()) {
             if (nodeEntity.viewType == 1 && TextUtils.isEmpty(nodeEntity.content)){
                 Toast.makeText(context,nodeEntity.title+"内容必须填写",Toast.LENGTH_SHORT).show();
                jumpToPosition(nodeEntity,rlv);
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
    * */
    private void jumpToPosition(NodeEntity nodeEntity, RecyclerView rlv) {
        if (nodeEntity.isShow){
            rlv.scrollToPosition(nodeEntity.position);
        }else {
            nodeEntity.isShow = true;
            for (NodeEntity entity : map.values()) {
                if (nodeEntity.sequence.startsWith(entity.sequence)||(nodeEntity.sequence.startsWith(entity.parentId))) {
                    entity.isShow = true;
                    entity.isOpen = true;
                }
            }
            dataChanged();
            rlv.scrollToPosition(nodeEntity.position);
        }

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
    *
    * */
    private boolean isSetEt = false;
    public class MyTextWatcher implements TextWatcher {
        private int index = -1;  //位置
        private int type = -1;  //赋值类型

        public MyTextWatcher(int position) {
            index = position;
        }
        public void  upIndex(int position){
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
            if (isSetEt)return;
            datas.get(index).content = s.toString();
        }
    }

}
