package com.example.morelevelslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView rlv;
    private LevelsAdapter2 adapter;
    /**
     * 获取数据
     */
    private List<NodeEntity> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
        getData();
    }
    private void getData() {
//        人工组成一个五级数据吧
        datas = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            NodeEntity nodeEntity1 = new NodeEntity();
            nodeEntity1.sequence = i + "";
            nodeEntity1.title = i + ".一級節點";
            if (i == 5) {
                nodeEntity1.viewType = 1;
            } else {
                nodeEntity1.viewType = -1;
            }
            nodeEntity1.content = "輸入内容在" + nodeEntity1.sequence;
            nodeEntity1.isOpen = false;
            nodeEntity1.isShow = true;
            datas.add(nodeEntity1);
            for (int j = 1; j < 7; j++) {
                NodeEntity nodeEntity2 = new NodeEntity();
                nodeEntity2.sequence = i + "." + j;
                nodeEntity2.parentId = i + "";
                if (j == 6) {
                    nodeEntity2.viewType = 1;
                } else {
                    nodeEntity2.viewType = -1;
                }
                nodeEntity2.title = nodeEntity2.sequence + ".二級節點";
                nodeEntity2.content = "輸入内容在" + nodeEntity2.sequence;
                nodeEntity2.isOpen = false;
                datas.add(nodeEntity2);

            }
        }
        rlv.setAdapter(adapter = new LevelsAdapter2(datas, this));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(rlv);
    }


    private void initView() {
        rlv = (RecyclerView) findViewById(R.id.rlv);
        rlv.setLayoutManager(new LinearLayoutManager(this));

    }
}