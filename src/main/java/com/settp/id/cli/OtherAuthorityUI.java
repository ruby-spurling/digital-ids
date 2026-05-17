package com.settp.id.cli;

import com.settp.id.core.model.Organisation;
import com.settp.id.core.service.OtherAuthority;

import java.util.Map;
import java.util.Scanner;

public class OtherAuthorityUI {
    private final OtherAuthority verificationService;
    private final Scanner scanner;
    private final Organisation userRole;

    public OtherAuthorityUI(OtherAuthority verificationService, Scanner scanner, Organisation userRole) {
        this.verificationService = verificationService;
        this.scanner = scanner;
        this.userRole = userRole;
    }

    public void printMenu() {
        System.out.printf("\n %s Menu%n", userRole.name());
        System.out.println("1. View all ID data");
        System.out.println("2. Check if status is valid");
        System.out.println("3. Exit");
        System.out.println("Select an option: ");
    }

    public boolean handleChoice(String choice) {
        switch (choice) {
            case "1":
                viewIdentity();
                return true;
            case "2":
                doQuickCheck();
                return true;
            case "3":
                return false;
            default:
                System.out.println("[ERROR] Invalid option, enter a number from 1-3");
                return true;
        }
    }

    private void viewIdentity() {
        System.out.println("Enter UUID to view: ");
        String uuid = scanner.nextLine();

        Map<String, String> uuidData = verificationService.verifyIdentity(uuid, userRole);

        System.out.println("\n--- ID Record ---");
        uuidData.forEach((key, value) -> System.out.println("- " + key + ": " + value));
        System.out.println("-----------------------");
    }

    private void doQuickCheck() {
        System.out.println("Enter UUID to check status of: ");
        String uuid = scanner.nextLine();

        boolean isValid = verificationService.statusValidityCheck(uuid);
        System.out.println("\n--- Status Check ---");
        System.out.println("Is the ID ACTIVE? " + (isValid ? "YES" : "NO"));
        System.out.println("-----------------------");
    }
}
