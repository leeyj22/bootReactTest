export const backUrl =
    process.env.NEXT_PUBLIC_ENV === "production"
        ? process.env.NEXT_PUBLIC_BASE_URL
        : process.env.NEXT_PUBLIC_BASE_URL;
