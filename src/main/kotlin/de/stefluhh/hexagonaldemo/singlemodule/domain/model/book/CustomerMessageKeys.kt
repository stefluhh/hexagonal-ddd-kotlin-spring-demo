package de.stefluhh.hexagonaldemo.singlemodule.domain.model.book

enum class CustomerMessageKeys(val message: String) {

    THANK_YOU_FOR_RETURNING("Thank you for returning the book in good condition!"),
    SLIGHT_PENALTY("Thank you for returning the book. Please note that we have to charge a penalty due to significant wear."),
    SIGNIFICANT_PENALTY("Thank you for returning the book. Please note that we have to charge a penalty due to significant wear."),
    PLESE_DONT_BORROW_AGAIN("Thank you for returning the book. Please note that we have to charge the full price of the book due to significant wear. Please don't borrow books at our library again :-)"),

}