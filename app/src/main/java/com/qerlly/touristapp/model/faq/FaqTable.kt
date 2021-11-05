package com.qerlly.touristapp.model.faq

enum class FaqTable(val id: String, val question: String, val answer: String) {
    FIRST("1", "Question 1", "Answer 1"),
    SECOND("2", "Question 2", "Answer 2"),
    THIRD("3", "Question 3", "Answer 3");
}