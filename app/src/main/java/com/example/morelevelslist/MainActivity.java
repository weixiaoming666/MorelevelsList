package com.example.morelevelslist;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
// 完成
public class MainActivity extends AppCompatActivity {
    private RecyclerView rlv;
    private LevelsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
    }

    /**
     * 获取数据
     */
    private List<NodeEntity> datas;

    private void getData() {
//        人工组成一个五级数据吧
        datas = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
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
                for (int k = 1; k < 8; k++) {
                    NodeEntity nodeEntity3 = new NodeEntity();
                    nodeEntity3.sequence = i + "." + j + "." + k;
                    nodeEntity3.parentId = i + "." + j;
                    nodeEntity3.title = nodeEntity3.sequence + ".三級節點";
                    if (k == 7) {
                        nodeEntity3.viewType = 1;
                    } else {
                        nodeEntity3.viewType = -1;
                    }
                    nodeEntity3.content = "輸入内容在" + nodeEntity3.sequence;
                    nodeEntity3.isOpen = false;
                    datas.add(nodeEntity3);
                    for (int l = 1; l < 8; l++) {
                        NodeEntity nodeEntity4 = new NodeEntity();
                        nodeEntity4.sequence = i + "." + j + "." + k + "." + l;
                        nodeEntity4.parentId = i + "." + j + "." + k;
                        nodeEntity4.title = nodeEntity4.sequence + ".四級節點";
                        if (l == 6) {
                            nodeEntity4.viewType = 1;
                        } else {
                            nodeEntity4.viewType = -1;
                        }
                        nodeEntity4.content = "輸入内容在" + nodeEntity4.sequence;
                        nodeEntity4.isOpen = false;
                        datas.add(nodeEntity4);
                        for (int m = 1; m < 7; m++) {
                            NodeEntity nodeEntity5 = new NodeEntity();
                            nodeEntity5.sequence = i + "." + j + "." + k + "." + l + "." + m;
                            nodeEntity5.parentId = i + "." + j + "." + k + "." + l;
                            nodeEntity5.title = nodeEntity5.sequence + ".請輸入你的想法";
                            nodeEntity5.viewType = 1;
                            nodeEntity5.content = "";
                            nodeEntity5.isOpen = false;
                            datas.add(nodeEntity5);
                        }
                    }

                }
            }
        }
        rlv.setAdapter(adapter = new LevelsAdapter(datas, this));

    }


    private void initView() {
        rlv = (RecyclerView) findViewById(R.id.rlv);
        rlv.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 提交操作；处理未填写数据的地方
     */
    public void sumbit(View view) {
        if (adapter != null) {
            adapter.getDatas(rlv);
        }

    }
}