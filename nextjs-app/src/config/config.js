export const backUrl =
  process.env.NODE_ENV === "production"
    ? process.env.BASE_URL
    : process.env.BASE_URL;
