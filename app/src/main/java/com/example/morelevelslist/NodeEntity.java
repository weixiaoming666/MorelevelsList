package com.example.morelevelslist;


import androidx.annotation.InspectableProperty;
import androidx.annotation.Nullable;

public class NodeEntity {
    public String  sequence ;//序号
    public String  title ;//序号
    public String  content ;//内容
    public int  viewType ;//类型
    public int  id ;//类型
    public String  parentId = "-1" ;//父节点  -1初始节点 其他则是父节点的sequence
    public boolean  isOpen ;//是否打开
    public boolean  isShow  = false;//是否顯示
    public int  position ;//位置


    @Override
    public String toString() {
        return "序号{" +
                "sequence='" + sequence +
                "parentId='" + parentId +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        NodeEntity entity = (NodeEntity) obj;
        if (entity.sequence.equals(this.sequence)){
            return true;
        }else {
            return false;
        }

    }
}
