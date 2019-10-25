const path = require("path");
const webpack = require("webpack");

const SRC_DIR = "./src/main/resources/static/src/pages/";

module.exports = {
    entry: {
        friends: SRC_DIR + "friends.js",
    },
    output: {
        path: path.join(__dirname, "/src/main/resources/static/out"),
        filename: "[name].js"
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                },
            },
            {
                test: /\.css$/,
                use: ["style-loader", "css-loader"]
            },
            {
                test: /\.jsx$/,
                use: 'babel-loader'
            }
        ],
    },
    resolve: {
        extensions: ['.js', '.jsx']
    },
    plugins: [
        new webpack.ProvidePlugin({
            "React": "react",
        }),
    ],
};