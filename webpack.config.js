const path = require('path');

module.exports = {
    entry: './src/main/js/index.js',
    devtool: 'source-map',
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            presets: ["@babel/preset-env", "@babel/preset-react"]
                        }
                    }],
                resolve: {
                    extensions: ['.js', '.jsx', '.css']
                },
            },
            {
                test: /\.css$/,
                use: [
                    "style-loader",
                    {
                        loader: "css-loader",
                        options: {
                            modules: true,
                            sourceMap: true,
                            importLoaders: 1,
                        }
                    },
                    "postcss-loader" // has separate config, see postcss.config.js nearby
                ]
            },
            {
                test: /\.(png|jpg|jpeg|gif)$/,
                loader: 'file-loader'
            }
        ],

    }
};