package com.example.morelevelslist;


import androidx.recyclerview.widget.RecyclerView;

/**
    * 定义RecycleView的Adapter和SimpleItemTouchHelperCallback直接交互的接口方法
    * Created by wxm on 2021/12/29
    */

public interface ItemTouchHelperAdapter {

//    /数据交换
void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

//数据删除
void onItemDissmiss(RecyclerView.ViewHolder source);

//drag或者swipe选中
void onItemSelect(RecyclerView.ViewHolder source, int actionState);

//状态清除
void onItemClear(RecyclerView.ViewHolder source);

//状态清除
boolean canDropOver(int from,int go);
}
