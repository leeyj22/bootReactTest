// fileUtils.js
export const fileToSize = (fileSize) => {
    if (fileSize === 0) return "0 바이트";

    const units = ["바이트", "KB", "MB", "GB", "TB"];
    const base = 1024;
    const unitIndex = Math.floor(Math.log(fileSize) / Math.log(base));
    const sizeInUnit = (fileSize / Math.pow(base, unitIndex)).toFixed(2);

    return `${sizeInUnit} ${units[unitIndex]}`;
};
