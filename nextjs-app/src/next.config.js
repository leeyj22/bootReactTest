const withBundleAnalyzer = require("@next/bundle-analyzer")({
  enabled: process.env.ANALYZE === "true",
});

module.exports = withBundleAnalyzer({
  compress: true,
  webpack(config) {
    const prod = process.env.NODE_ENV === "production";
    // const plugins = [...config.plugins];
    return {
      ...config,
      mode: prod ? "production" : "development",
      devtool: prod ? "hidden-source-map" : "eval",
      plugins: [...config.plugins],
    };
  },
});

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  images: {
    unoptimized: true,
  },
  env: {
    BASE_URL: process.env.BASE_URL,
  },
  async rewrites() {
    return [
      {
        source: "/:path*",
        destination: `${process.env.BASE_URL}/api/:path*`,
      },
    ];
  },
};

module.exports = nextConfig;
