package com.luxoft.bankapp.email;

import com.luxoft.bankapp.domain.Client;

import java.util.List;

public class Email {
    private Client from;
    private List<Client> tos;
    private List<Client> ccs;
    private String subject;
    private String body;

    public Email(Client from, List<Client> tos, List<Client> ccs, String subject, String body) {
        this.from = from;
        this.tos = tos;
        this.ccs = ccs;
        this.subject = subject;
        this.body = body;
    }

    public Client getFrom() {
        return from;
    }

    public List<Client> getTos() {
        return tos;
    }

    public List<Client> getCcs() {
        return ccs;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Email{" +
                "from=" + from +
                ", tos=" + tos +
                ", ccs=" + ccs +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
