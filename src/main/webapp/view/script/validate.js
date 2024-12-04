function validatePhone(input) {


    if (input.value === "") {
    input.value = "+7";
}


    if (!input.value.startsWith("+7")) {
    input.value = "+7".concat("", input.value);
}


    input.value = input.value.replace(/[^\d+]/g, '');
}

