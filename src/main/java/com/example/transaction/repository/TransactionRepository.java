package com.example.transaction.repository;


import com.example.transaction.model.Transaction;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {
    private final Node head = new Node(null);
    private final Node tail = new Node(null);
    private ConcurrentHashMap<Integer, Node> map = new ConcurrentHashMap<>();

    static class Node
    {
        Transaction value;
        Node next;
        Node prev;
        public Node (Transaction value) {
            this.value=value;
        }
    }

    @PostConstruct
    public void init() {
        head.next = tail;
        tail.prev = head;
    }

    //时间复杂度O(1)
    public void insert(Transaction transaction) throws InterruptedException{
        // 检查是否已经存在
        if (map.containsKey(transaction.getId())) {
            throw new InterruptedException("Transaction already exists");
        }
        // 新建一个节点
        Node newNode = new Node(transaction);
        // 插入到链表
        tail.prev.next = newNode;
        newNode.next = tail;
        newNode.prev = tail.prev;
        tail.prev = newNode;
        // 存入map
        map.put(newNode.value.getId(), newNode);
    }

    //时间复杂度O(1)
    public void delete(int id) throws InterruptedException{
        // 检查是否已经存在
        if (!map.containsKey(id)) {
            throw new InterruptedException("Transaction id does not exist");
        }
        // 从链表删除
        Node node = map.get(id);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        // 从map删除
        map.remove(id);
    }

    //时间复杂度O(1)
    public void update(Transaction transaction) throws InterruptedException{
        int id = transaction.getId();
        // 检查是否已经存在
        if (!map.containsKey(id)) {
            throw new InterruptedException("Transaction id does not exist");
        }
        // 更新
        Node node = map.get(id);
        node.value = transaction;
    }

    //时间复杂度O(n)
    public TransactionPage search(int start, int size) throws InterruptedException{
        if (start < 0 || size < 1) {
            throw new InterruptedException("Invalid start or size");
        }
        Node current = head;
        TransactionPage transactionPage = new TransactionPage(new ArrayList<>(), map.size(), start / size, size);
        while (start > 0) {
            current = current.next;
            start--;
            if (current == tail) {
                return transactionPage;
            }
        }
        while (size > 0) {
            if (current == tail) {
                return transactionPage;
            }
            transactionPage.transactions.add(current.value);
            current = current.next;
            size--;
        }
        return transactionPage;
    }
}