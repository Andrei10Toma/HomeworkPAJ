package com.luxoft.bankapp.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Queue {
    private final List<Email> emailsToBeSent = Collections.synchronizedList(new ArrayList<>());

    public void add(Email email) {
        emailsToBeSent.add(email);
    }

    public Email get() {
        if (emailsToBeSent.isEmpty())
            return null;
        return emailsToBeSent.remove(emailsToBeSent.size() - 1);
    }
}
