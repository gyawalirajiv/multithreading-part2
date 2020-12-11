package com.rajiv;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Prayer prayer = new Prayer();
        (new Thread(new Human(prayer))).start();
        (new Thread(new God(prayer))).start();
    }
}

class Prayer {
    private String prayer;
    private boolean empty = true;

    public synchronized String read(){
        while (empty){
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        empty = true;
        notifyAll();
        return prayer;
    }

    public synchronized void write(String prayer){
        while (!empty){
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        empty = false;
        this.prayer = prayer;
        notifyAll();
    }
}

class Human implements Runnable{
    private Prayer prayer;

    public Human(Prayer prayer) {
        this.prayer = prayer;
    }

    @Override
    public void run() {
        String prayers[] = {
                "I want a big house",
                "and a beautiful wife",
                "and a sports car",
                "I want a world peace"
        };

        Random random = new Random();
        for (int i = 0; i < prayers.length; i++) {
            prayer.write(prayers[i]);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {}
        }
        prayer.write("Satisfied");
    }
}

class God implements Runnable {
    private Prayer prayer;

    public God(Prayer prayer) {
        this.prayer = prayer;
    }

    @Override
    public void run() {
        Random random = new Random();
        for(String latestPrayer = prayer.read(); !latestPrayer.equals("Satisfied"); latestPrayer = prayer.read()){
            System.out.println(latestPrayer);
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {}
        }
    }
}