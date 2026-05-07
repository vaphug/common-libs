package com.yourdomain.demo.api;

import com.yourdomain.common.validation.annotation.ItemValidate;
import com.yourdomain.common.validation.annotation.HalfWidth;
import com.yourdomain.common.validation.annotation.FullWidth;
import com.yourdomain.common.validation.annotation.Regex;
import com.yourdomain.common.validation.annotation.ValidPhoneNumber;

public record PhoneValidationRequest(
        @ValidPhoneNumber String phoneNumber,
        @ItemValidate(item = "textAny") String textAny,
        @ItemValidate(item = "textHalfWidth") String textHalfWidth,
        @ItemValidate(item = "textFullWidth") String textFullWidth,
        @ItemValidate(item = "textFullAndHalf") String textFullAndHalf,
        @ItemValidate(item = "number") String number,
        @ItemValidate(item = "numberWithMinus") String numberWithMinus,
        @ItemValidate(item = "digitsOnly") String digitsOnly,
        @ItemValidate(item = "amount") String amount,
        @ItemValidate(item = "amountWithMinus") String amountWithMinus,
        @ItemValidate(item = "amountMinusOnly") String amountMinusOnly,
        @ItemValidate(item = "numberGtZero") String numberGtZero,
        @ItemValidate(item = "amountGtZero") String amountGtZero,
        @ItemValidate(item = "dateYyyy") String dateYyyy,
        @ItemValidate(item = "dateYyyymm") String dateYyyymm,
        @ItemValidate(item = "dateYyyymmdd") String dateYyyymmdd,
        @ItemValidate(item = "dateTime") String dateTime,
        @ItemValidate(item = "category") String category,
        @ItemValidate(item = "currency") String currency,
        @HalfWidth String halfWidthDirect,
        @FullWidth String fullWidthDirect,
        @Regex(pattern = "^[A-Z]{3}[0-9]{3}$") String regexDirect
) {
}
