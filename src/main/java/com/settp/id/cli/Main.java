package com.settp.id.cli;

import com.settp.id.core.repository.IdentityRepository;
import com.settp.id.core.repository.JsonIdentityRepository;
import com.settp.id.core.service.CentralAuthority;

public class Main {
    public static void main(String[] args) {
        IdentityRepository repository= new JsonIdentityRepository();
        CentralAuthority service = new CentralAuthority(repository);

        ConsoleApplication app = new ConsoleApplication(service);
        app.start();
    }
}
