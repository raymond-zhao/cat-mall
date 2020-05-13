package edu.dlut.catmall.search;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MallSearchApplicationTests {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void test() {
        System.out.println(restHighLevelClient);
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    @Test
    public void reConstructBinaryTree() {
        int[] pre = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] in = {4, 7, 2, 1, 5, 3, 8, 6};
        reConstructBinaryTree(pre, in);
    }

    // 缓存中序遍历数组每个值对应的索引
    private Map<Integer, Integer> indexForInOrders = new HashMap<>();

    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        // in: [4,7,2,1,5,3,8,6]
        for (int i = 0; i < in.length; i++)
            indexForInOrders.put(in[i], i);
        // (4,0),(7,1),(2,2),(1,3),(5,4),(3,5),(8,6),(6,7)
        return reConstructBinaryTree(pre, 0, pre.length - 1, 0);
    }

    private TreeNode reConstructBinaryTree(int[] pre, int preL, int preR, int inL) {
        // pre: [1,2,4,7,3,5,6,8] preL: 0, preR: 7, inL: 0
        if (preL > preR)
            return null;
        System.out.println("preL: " + preL);
        System.out.println("preR: " + preR);
        System.out.println("inL: " + inL);
        TreeNode root = new TreeNode(pre[preL]); // 让前序遍历中的第一个结点作为当前子树的根节点
        System.out.println("root.val: " + root.val);
        // 获取当前子树的根节点在中序遍历中所处的位置，以此划分左右子树。
        int inIndex = indexForInOrders.get(root.val); // 3
        System.out.println("inIndex: " + inIndex);
        int leftTreeSize = inIndex - inL; // 左子树的元素个数 3
        System.out.println("leftTreeSize: " + leftTreeSize);
        root.left = reConstructBinaryTree(pre, preL + 1, preL + leftTreeSize, inL); // 构建左子树
        // pre, 1, 3, 0
        root.right = reConstructBinaryTree(pre, preL + 1 + leftTreeSize, preR, inL + 1 + leftTreeSize); // 构建右子树
        // pre, 4, 7, 4
        return root;
    }

}
