//String prototype is read only, properties should not be added  no-extend-native

export const capitalizeFirstLetter = (string) => {
    return string.charAt(0).toUpperCase() + string.slice(1);
};