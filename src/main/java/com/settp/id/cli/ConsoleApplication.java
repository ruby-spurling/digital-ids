package com.settp.id.cli;

import com.settp.id.core.exception.DataAccessException;
import com.settp.id.core.exception.IdentityNotFoundException;
import com.settp.id.core.exception.IllegalStatusChangeException;
import com.settp.id.core.model.IdentityStatus;
import com.settp.id.core.service.CentralAuthority;

import java.util.Scanner;

public class ConsoleApplication {
    private final CentralAuthority service;
    private final Scanner scanner;

    public ConsoleApplication(CentralAuthority service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=========================================");
        System.out.println("   Digital Identity Management System    ");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                running = handleChoice(choice);
            } catch (IdentityNotFoundException | IllegalStatusChangeException e) {
                System.out.println("\n[REJECTED] " + e.getMessage());
            }  catch (DataAccessException e) {
                System.out.println("\n[ERROR] Database failure: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("\n[INVALID INPUT] Please provide a valid input");
            }
        }
        System.out.println("Shutting down system...");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\nOptions:");
        System.out.println("1. Create new ID");
        System.out.println("2. Update status of an ID");
        System.out.println("3. Update ID data");
        System.out.println("4. Exit");
        System.out.println("Select an option: ");
    }

    private boolean handleChoice(String choice) {
        switch (choice) {
            case "1":
                String newUuid = service.createIdentity();
                System.out.println("[Success] Created new identity with UUID: " + newUuid);
                return true;
            case "2":
                System.out.println("Enter UUID: ");
                String uuidToUpdate = scanner.nextLine();
                System.out.println("Enter new Status (ACTIVE, SUSPENDED, REVOKED): ");
                String statusStr = scanner.nextLine().toUpperCase();

                service.statusUpdate(uuidToUpdate, IdentityStatus.valueOf(statusStr));
                System.out.println("[SUCCESS] Status updated");
                return true;
            case "3":
                System.out.println("Enter UUID: ");
                String attrUuid = scanner.nextLine();
                System.out.println("Enter attribute to update: ");
                String attrName = scanner.nextLine();
                System.out.println("Enter updated value: ");
                String attrValue = scanner.nextLine();

                service.updateAttribute(attrUuid, attrName, attrValue);
                System.out.println("[SUCCESS] Attribute updated");
                return true;
            case "4":
                return false;
            default:
                System.out.println("[ERROR] Invalid option, enter a number from 1-4");
                return true;
        }

    }
}
