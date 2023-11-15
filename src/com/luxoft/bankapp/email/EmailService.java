package com.luxoft.bankapp.email;

public class EmailService implements Runnable {
    private final Queue queue = new Queue();
    private boolean closed;

    public Thread getThread() {
        return thread;
    }

    private Thread thread;

    public EmailService() {
        closed = false;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            if (closed)
                return;
            Email email = queue.get();

            if (email != null)
                sendEmail(email);

            try {
                synchronized (queue) {
                    queue.wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendEmail(Email email) {
        System.out.println(email);
    }

    public void sendNotificationEmail(Email email) {
        if (closed)
            return;
        queue.add(email);
        synchronized (queue) {
            queue.notify();
        }
    }

    public void close() {
        closed = true;
        synchronized (queue) {
            queue.notify();
        }
    }
}
