package com.projects.android.recipebook.model.enums

enum class PreparationTime(value: String) {
	FIVE_MIN("5 минут"), THIRTY_MIN("30 минут"), ONE_HOUR("1 час"), TWO_HOURS("2 часа"), FOUR_HOURS("4 часа"), UNLIMITED("Не ограничено");

	private val valueString: String = value

	override fun toString(): String {
		return valueString
	}
}
