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
        System.out.println("1. Create new ID");
        System.out.println("2. Update status of an ID");
        System.out.println("3. Update ID data");
        System.out.println("4. View ID data");
        System.out.println("5. Exit");
        System.out.println("Select an option: ");
    }

    public boolean handleChoice(String choice) {
        switch (choice) {
            case "1":
                createIdentity();
                return true;
            case "2":
                updateIdentityStatus();
                return true;
            case "3":
                updateSingleAttribute();
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

    private void createIdentity() {
        System.out.println("\n--- Creating new ID---");
        String newUuid = managementService.createIdentity(userRole);

        String name = "";
        while (name.trim().isEmpty()) {
            System.out.println("Enter full name: ");
            name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("[ERROR] Name is required, please enter a name");
            }
        }
        managementService.updateAttribute(newUuid, "name", name, userRole);

        String dob = "";
        while (dob.trim().isEmpty()) {
            System.out.println("Enter date of birth in the format YYYY-MM-DD: ");
            dob = scanner.nextLine();
            if (dob.trim().isEmpty()) {
                System.out.println("[ERROR] Date of birth is required, please enter a date");
            }
        }
        managementService.updateAttribute(newUuid, "date_of_birth", dob, userRole);

        getAdditionalDetails(newUuid);

        System.out.println("-----------------------");
        System.out.println("[Success] Created new identity with UUID: " + newUuid);
    }

    private void getAdditionalDetails(String uuid) {
        String[] optionalAttributes = {
                "right_to_work",
                "residency_status",
                "ni_number",
                "driving_restriction",
                "driving_license_category",
                "driving_penalty_points"
        };
        System.out.println("You will be prompted to add details for additional fields, press enter to skip any field.");
        for (String attribute : optionalAttributes) {
            System.out.println("Enter " + attribute);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                managementService.updateAttribute(uuid, attribute, value, userRole);
                System.out.println("[SUCCESS] " + attribute + " updated");
            }
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

    private void updateIdentityStatus() {
        System.out.println("Enter UUID: ");
        String uuidToUpdate = scanner.nextLine();
        System.out.println("Enter new Status (ACTIVE, SUSPENDED, REVOKED): ");
        String statusStr = scanner.nextLine().toUpperCase();
        managementService.statusUpdate(uuidToUpdate, IdentityStatus.valueOf(statusStr), userRole);
        System.out.println("[SUCCESS] Status updated");
    }

    private void updateSingleAttribute() {
        System.out.println("Enter UUID: ");
        String uuid = scanner.nextLine();
        System.out.print("Enter new attribute to update: ");
        String key = scanner.nextLine();
        System.out.print("Enter new value: ");
        String value = scanner.nextLine();
        managementService.updateAttribute(uuid, key, value, userRole);
        System.out.println("[SUCCESS] Attribute updated successfully.");
    }
}
