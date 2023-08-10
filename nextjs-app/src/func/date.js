export const dateFunc = {
    dateAdd: (datepart, number) => {
        var yy,
            mm,
            dd,
            date = this;

        yy = date.getFullYear();
        mm = date.getMonth();
        dd = date.getDate();

        switch (datepart.toLowerCase()) {
            case "y":
                yy += number;
                break;
            case "m":
                mm += number;
                break;
            case "d":
                dd += number;
                break;
        }
        return new Date(yy, mm, dd);
    },
};
