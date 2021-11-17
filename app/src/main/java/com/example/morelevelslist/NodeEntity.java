package com.example.morelevelslist;


import androidx.annotation.InspectableProperty;

public class NodeEntity {
    public String  sequence ;//序号
    public String  title ;//序号
    public String  content ;//内容
    public int  viewType ;//类型
    public String  parentId = "-1" ;//父节点 0 初始节点
    public boolean  isOpen ;//是否打开
    public boolean  isShow  = false;//是否顯示
    public int  position ;//位置

}
