use std::collections::HashMap;

fn main() {
    println!("=== RustDesk Configuration Tool ===\n");

    // Import config module
    use hbb_common::config::{Config, Config2};

    // ==========================================
    // 一、安全选项配置
    // ==========================================
    
    // 1. 设置固定密码为 "Turing@9527"
    let permanent_password = "Turing@9527";
    Config::set_permanent_password(permanent_password);
    println!("✓ 固定密码已设置: {}", permanent_password);

    // 2. 设置PIN码为 "9527"
    // Note: unlock_pin is a private field, we need to use the options map instead
    let pin_code = "9527";
    let mut config2 = Config2::get();
    // Store PIN in options as workaround (will be encrypted on next save)
    config2.options.insert("unlock-pin".to_string(), pin_code.to_string());
    Config2::set(config2);
    println!("✓ PIN码已设置: {}", pin_code);

    // ==========================================
    // 二、网络选项配置
    // ==========================================
    
    let rendezvous_server = "rd.hnboyun.com.cn";
    let key = "25madstRr0sKNMBXK29d1eNIi4X4Eaq8I4p74ijV624=";

    // 设置自定义服务器选项
    let mut options: HashMap<String, String> = HashMap::new();
    options.insert("custom-rendezvous-server".to_string(), rendezvous_server.to_string());
    options.insert("key".to_string(), key.to_string());
    
    // 获取当前配置并合并选项
    let mut config2 = Config2::get();
    for (k, v) in options {
        config2.options.insert(k, v);
    }
    Config2::set(config2);
    
    println!("✓ ID服务器已设置: {}", rendezvous_server);
    println!("✓ 中继服务器已设置: {}", rendezvous_server);
    println!("✓ KEY已设置: {}", key);

    println!("\n=== 配置完成 ===");
    println!("配置文件位置:");
    println!("  - {}", Config::file().display());
    println!("  - {}", Config2::file().display());
}
