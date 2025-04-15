import Foundation

@objc public class Star: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
