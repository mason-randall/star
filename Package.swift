// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "Star",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "Star",
            targets: ["StarPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "StarPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/StarPlugin"),
        .testTarget(
            name: "StarPluginTests",
            dependencies: ["StarPlugin"],
            path: "ios/Tests/StarPluginTests")
    ]
)