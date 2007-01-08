package com.scully.korat.test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.scully.korat.IKoratObservable;
import com.scully.korat.IKoratObserver;
import com.scully.korat.KoratObserver;


public class SearchTreeInst implements IKoratObservable
{
    Node root; // root node

    int $kor_root; // korat

    int size; // number of nodes in the tree

    int $kor_size; // korat

    IKoratObserver $kor_observer; // korat;

    public static class Node implements IKoratObservable
    {
        Node left; // left child

        int $kor_left; // korat

        Node right; // right child

        int $kor_right; // korat

        int value;

        int $kor_value; // korat

        // TODO: Remove this when instrumentation is complete.
        // this can't be NULL when testing the method (it doesn't
        // get set then), so we initialize it here.
        IKoratObserver $kor_observer = new KoratObserver(); // korat;

        public void $kor_setObserver(IKoratObserver observer)
        {
            this.$kor_observer = observer;
        }

        public String toString()
        {
            return super.toString().substring(super.toString().indexOf('@') + 1);
        }

        /**
         * @return the $kor_left
         */
        public Node $kor_getLeft()
        {
            this.$kor_observer.notify(this.$kor_left);
            return this.left;
        }

        /**
         * @param $kor_left the $kor_left to set
         */
        public void $kor_setLeft(Node left)
        {
            this.$kor_observer.notify(this.$kor_left);
            this.left = left;
        }

        /**
         * @return the $kor_right
         */
        public Node $kor_getRight()
        {
            this.$kor_observer.notify(this.$kor_right);
            return this.right;
        }

        /**
         * @param $kor_right the $kor_right to set
         */
        public void $kor_setRight(Node right)
        {
            this.$kor_observer.notify(this.$kor_right);
            this.right = right;
        }

        /**
         * @return the $kor_value
         */
        public int $kor_getValue()
        {
            this.$kor_observer.notify(this.$kor_value);
            return this.value;
        }

        /**
         * @param $kor_value the $kor_value to set
         */
        public void $kor_setValue(int value)
        {
            this.$kor_observer.notify(this.$kor_value);
            this.value = value;
        }
    }

    public void $kor_setObserver(IKoratObserver observer)
    {
        this.$kor_observer = observer;
    }

    public String toString()
    {
        return super.toString().substring(super.toString().indexOf('@') + 1);
    }

    /*@ normal_behavior // non-exceptional specification
     @ // precondition
     @ requires repOk();
     @ // postcondition
     @ ensures repOk() && !contains(value) &&
     @ \result == \old(contains(value));
     @*/
    public boolean remove(int value)
    {
        /*/
         if(root != null && root.left != null)
         {
         root.left = root.right;
         }
         return true;
         /*/
        Node parent = null;
        Node current = root;
        //        if(size == 2) size = 3;
        while (current != null)
        {
            //			int cmp = info.compareTo(current.info);
            if (value < current.value)
            {
                parent = current;
                current = current.left;
            }
            else if (value > current.value)
            {
                parent = current;
                current = current.right;
            }
            else
            {
                break;
            }
        }
        if (current == null)
            return false;
        Node change = removeNode(current);
        if (parent == null)
        {
            root = change;
        }
        else if (parent.left == current)
        {
            parent.left = change;
        }
        else
        {
            parent.right = change;
        }
        return true;
        //*/
    }

    Node removeNode(Node current)
    {
        size--;
        Node left = current.left, right = current.right;
        if (left == null)
            return right;
        if (right == null)
            return left;
        if (left.right == null)
        {
            current.value = left.value;
            current.left = left.left;
            return current;
        }
        Node temp = left;
        while (temp.right.right != null)
        {
            temp = temp.right;
        }
        current.value = temp.right.value;
        temp.right = temp.right.left;
        return current;
    }

    /*@ pure @*/boolean contains(int value)
    {
//        Node parent = null;
        Node current = root;
        while (current != null)
        {
            if (value < current.value)
            {
//                parent = current;
                current = current.left;
            }
            else if (value > current.value)
            {
//                parent = current;
                current = current.right;
            }
            else
            {
                break;
            }
        }
        if (current == null)
            return false;
        return true;
    }

    /*@ pure @*/boolean repOk()
    {
        // checks that empty tree has size zero
//        if (root == null)
//            return size == 0;
        if ($kor_getRoot() == null)
            return $kor_getSize() == 0;
        // checks that the input is a tree
        if (!isAcyclic())
            return false;
        // checks that size is consistent
//        if (numNodes(root) != size)
        if (numNodes($kor_getRoot()) != $kor_getSize())
            return false;
        // checks that data is ordered
//        if (!isOrdered(root))
        if (!isOrdered($kor_getRoot()))
            return false;
        return true;
    }

    /*@ pure @*/boolean isAcyclic()
    {
        Set visited = new HashSet();
//        visited.add(root);
        visited.add($kor_getRoot());
        LinkedList workList = new LinkedList();
//        workList.add(root);
        workList.add($kor_getRoot());
        while (!workList.isEmpty())
        {
            Node current = (Node) workList.removeFirst();
//            if (current.left != null)
            if (current.$kor_getLeft() != null)
            {
                // checks that the tree has no cycle
//                if (!visited.add(current.left))
                if (!visited.add(current.$kor_getLeft()))
                    return false;
//                workList.add(current.left);
                workList.add(current.$kor_getLeft());
            }
//            if (current.right != null)
            if (current.$kor_getRight() != null)
            {
                // checks that the tree has no cycle
//                if (!visited.add(current.right))
                if (!visited.add(current.$kor_getRight()))
                    return false;
//                workList.add(current.right);
                workList.add(current.$kor_getRight());
            }
        }
        return true;
    }

    /*@ pure @*/int numNodes(Node n)
    {
        if (n == null)
            return 0;
//        return 1 + numNodes(n.left) + numNodes(n.right);
        return 1 + numNodes(n.$kor_getLeft()) + numNodes(n.$kor_getRight());
    }

    /*@ pure @*/boolean isOrdered(Node n)
    {
        return isOrdered(n, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    /*@ pure @*/boolean isOrdered(Node n, int min, int max)
    {
//        if ((min != Integer.MAX_VALUE && n.value <= min) || (max != Integer.MIN_VALUE && n.value >= max))
        if ((min != Integer.MAX_VALUE && n.$kor_getValue() <= min) || (max != Integer.MIN_VALUE && n.$kor_getValue() >= max))
            return false;
//        if (n.left != null)
        if (n.$kor_getLeft() != null)
//            if (!isOrdered(n.left, min, n.value))
            if (!isOrdered(n.$kor_getLeft(), min, n.$kor_getValue()))
                return false;
//        if (n.right != null)
        if (n.$kor_getRight() != null)
//            if (!isOrdered(n.right, n.value, max))
            if (!isOrdered(n.$kor_getRight(), n.$kor_getValue(), max))
                return false;
        return true;
    }

    /**
     * @return the $kor_root
     */
    public Node $kor_getRoot()
    {
        this.$kor_observer.notify(this.$kor_root);
        return this.root;
    }

    /**
     * @param $kor_root the $kor_root to set
     */
    public void $kor_setRoot(Node root)
    {
        this.$kor_observer.notify(this.$kor_root);
        this.root = root;
    }

    /**
     * @return the $kor_size
     */
    public int $kor_getSize()
    {
        this.$kor_observer.notify(this.$kor_size);
        return this.size;
    }

    /**
     * @param $kor_size the $kor_size to set
     */
    public void $kor_setSize(int size)
    {
        this.$kor_observer.notify(this.$kor_size);
        this.size = size;
    }
}
