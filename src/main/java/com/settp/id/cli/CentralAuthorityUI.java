package com.settp.id.cli;

import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.model.Organisation;
import com.settp.id.core.service.CentralAuthority;
import com.settp.id.core.service.OtherAuthority;

import java.util.Map;
import java.util.Scanner;

public class CentralAuthorityUI {
    private final CentralAuthority managementService;
    private final OtherAuthority verificationService;
    private final Scanner scanner;
    private final Organisation userRole;

    public CentralAuthorityUI(CentralAuthority managementService, OtherAuthority verificationService, Scanner scanner, Organisation userRole) {
        this.managementService = managementService;
        this.verificationService = verificationService;
        this.scanner = scanner;
        this.userRole = userRole;
    }

    public void printMenu() {
        System.out.printf("\n %s Menu%n", userRole.name());

        if (userRole == Organisation.CENTRAL_AUTHORITY) {
            System.out.println("1. Create new ID");
            System.out.println("2. Update status of an ID");
            System.out.println("3. Update ID data");
            System.out.println("4. View ID data");
            System.out.println("5. Exit");
        } else {
            System.out.println("1. View all ID data");
            System.out.println("2. Check if status is valid");
            System.out.println("3. Exit");
        }
        System.out.println("Select an option: ");
    }

    public boolean handleChoice(String choice) {
        switch (choice) {
            case "1":
                String newUuid = managementService.createIdentity(userRole);
                System.out.println("[Success] Created new identity with UUID: " + newUuid);
                return true;
            case "2":
                System.out.println("Enter UUID: ");
                String uuidToUpdate = scanner.nextLine();
                System.out.println("Enter new Status (ACTIVE, SUSPENDED, REVOKED): ");
                String statusStr = scanner.nextLine().toUpperCase();

                managementService.statusUpdate(uuidToUpdate, IdentityStatus.valueOf(statusStr), userRole);
                System.out.println("[SUCCESS] Status updated");
                return true;
            case "3":
                System.out.println("Enter UUID: ");
                String attrUuid = scanner.nextLine();
                System.out.println("Enter attribute to update: ");
                String attrName = scanner.nextLine();
                System.out.println("Enter updated value: ");
                String attrValue = scanner.nextLine();

                managementService.updateAttribute(attrUuid, attrName, attrValue, userRole);
                System.out.println("[SUCCESS] Attribute updated");
                return true;
            case "4":
                viewIdentity();
                return true;
            case "5":
                return false;
            default:
                System.out.println("[ERROR] Invalid option, enter a number from 1-5");
                return true;
        }
    }

    private void viewIdentity() {
        System.out.println("Enter UUID to view: ");
        String uuid = scanner.nextLine();

        Map<String, String> uuidData = verificationService.verifyIdentity(uuid, userRole);

        System.out.println("\n--- Central Authority ID View ---");
        System.out.println("UUID: " + uuid);
        uuidData.forEach((key, value) -> System.out.println("- " + key + ": " + value));
        System.out.println("-----------------------");
    }
}
