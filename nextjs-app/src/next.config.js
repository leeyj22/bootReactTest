const withBundleAnalyzer = require("@next/bundle-analyzer")({
    enabled: process.env.ANALYZE === "true",
});

const nextConfig = {
    reactStrictMode: true,
    images: {
        unoptimized: true,
    },
    env: {
        BASE_URL: process.env.BASE_URL,
    },
    compiler: {
        styledComponents: true,
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

module.exports = withBundleAnalyzer({
    ...nextConfig,
    compress: true,
    webpack(config) {
        const prod = process.env.NODE_ENV === "production";
        const plugins = [...config.plugins];
        return {
            ...config,
            mode: prod ? "production" : "development",
            devtool: prod ? "hidden-source-map" : "eval",
            plugins: [...config.plugins],
        };
    },
});
