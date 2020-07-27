package com.github.DenFade.Saturn.entity.game;

import com.github.DenFade.Saturn.entity.IEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class MineSweeper extends IEntity {

    private final int x;
    private final int y;
    private final int b;
    private final int size;
    private boolean end = false;
    private final Cell[][] table;
    private static final int BOMB = 9;
    private static final int[][] around = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    private final String ownerId;
    private String messageId;

    public MineSweeper(int x, int y, int b, String ownerId){
        this.x = x;
        this.y = y;
        this.b = b;
        this.size = x*y;
        this.table = new Cell[y][x];
        this.ownerId = ownerId;
        for(int i = 0; i < this.y; i++){
            for(int k = 0; k < this.x; k++){
                this.table[i][k] = new Cell(0, false, false);
            }
        }
    }

    protected boolean isSafe(int x, int y){
        return x >= 0 && x < this.x && y >= 0 && y < this.y;
    }

    protected int[] splitC(int xy){
        return (new int[]{xy%this.y, xy/this.y});
    }

    protected int sumC(int x, int y){
        return x+(y*this.x);
    }

    protected void access3x3(int x, int y, BiConsumer<Integer, Integer> task){
        for(int[] r : around){
            int nx = x + r[0];
            int ny = y + r[1];
            if(isSafe(nx, ny)) task.accept(nx, ny);
        }
    }

    protected int access3x3s(int x, int y, BiFunction<Integer, Integer, Boolean> task){
        int s = 0;
        for(int[] r : around){
            int nx = x + r[0];
            int ny = y + r[1];
            if(isSafe(nx, ny)) {
                if(task.apply(nx, ny)) s++;
            }
        }
        return s;
    }

    public void setMine(){
        ArrayList<Integer> arr = new ArrayList<>();
        List<Integer> bombs;
        for(int i = 0; i < this.size; i++) arr.add(i);
        Collections.shuffle(arr);

        bombs = arr.subList(0, this.b);
        for(int c : bombs){
            int[] r = splitC(c);
            this.table[r[1]][r[0]].tile = BOMB;
            access3x3(r[0], r[1], (x,y) -> {
                if(this.table[y][x].tile != BOMB) this.table[y][x].tile += 1;
            });
        }
    }

    public void open(int x, int y){
        if(!isSafe(x, y)) return;
        else if(this.table[y][x].opened){
            if(this.table[y][x].tile > 0 && this.table[y][x].tile < 9){
                int count = access3x3s(x, y, (nx, ny) -> this.table[ny][nx].flag);
                if(count == this.table[y][x].tile){
                    access3x3(x, y, (cx, cy) -> {
                        if(!this.table[cy][cx].flag) open(cx, cy);
                    });
                }
            }
        } else {
            if(this.table[y][x].tile == BOMB){
                this.end = true;
            } else if(this.table[y][x].tile == 0){
                this.table[y][x].opened = true;
                access3x3(x, y, this::open);
            } else {
                this.table[y][x].opened = true;
            }
        }
    }

    public void flagging(int x, int y){
        if(!isSafe(x, y)) return;
        else {
            Cell cur = this.table[y][x];
            if(!cur.opened){
                this.table[y][x].flag = !cur.flag;
            }
        }
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    private static class Cell{

        int tile;
        boolean flag;
        boolean opened;

        Cell(int t,boolean f,boolean o){
            this.tile = t;
            this.flag = f;
            this.opened = o;
        }

        public int get(){
            return this.tile;
        }

        public boolean isFlag(){
            return this.flag;
        }

        public boolean isOpened(){
            return this.opened;
        }
    }

    @Override
    public String toString() {
        StringBuilder t = new StringBuilder();
        for(int i = 0; i < this.y; i++){
            for(int k = 0; k < this.x; k++){
                t.append(this.table[i][k].tile);
            }
            t.append("\n");
        }
        return "-- "+this.x+"x"+this.y+"/"+this.b+" Game--\n\n"+t;

    }
}