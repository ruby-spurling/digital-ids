package com.settp.id;

import com.settp.id.cli.ConsoleApplication;
import com.settp.id.core.repository.IdentityRepository;
import com.settp.id.core.repository.JsonIdentityRepository;
import com.settp.id.core.service.CentralAuthority;
import com.settp.id.core.service.OtherAuthority;

public class Main {
    public static void main(String[] args) {
        IdentityRepository repository= new JsonIdentityRepository();
        CentralAuthority managementService = new CentralAuthority(repository);
        OtherAuthority verificationService = new OtherAuthority(repository);

        ConsoleApplication app = new ConsoleApplication(managementService, verificationService);
        app.start();
    }
}
