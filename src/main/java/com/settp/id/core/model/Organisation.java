package com.settp.id.core.model;

public enum Organisation {
    CENTRAL_AUTHORITY, // Read/write access
    EMPLOYER, // Verifies validity of ID, right to work, over 18
    TAX_SERVICE, // Tax residency status (domestic/international), NI number
    DRIVING_LICENSE_AUTHORITY, // Verifies status of ID, driving restrictions (maybe like is suspended?), driving license category, penalty points
    BANK // Only checks if an ID is valid/active
}
