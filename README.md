# SendText
Send texts from your Android phone to your Windows computer in the same network. ~~This is also my first WPF program.~~

- Maybe there will be a Mac client with Xamarin
- IOS app might be $1 if I want to write it

# Motivation
Sometimes, I need to send messages to my friend in Chinese when I am playing World of Warships. However, the game doesn't allow me to do that and I can't even see all Chinese characters because I set the language to Japanese. Worry no more. I can `SendText` to my computer and my wpf program will simply copy it to the clipboard. It might also be useful if you want to send a URL to your computer. 

# Limitation
1. SendText won't work in certain networks (like university wifi) but home network should be fine (hotspot is ok as well). Maybe it is because of the firewall? but I am not really sure
2. SendText only refreshes every 3 seconds so there might be some delay (but usually it is fine and you won't even notice it at all)

# Screenshot
![WPF](https://raw.githubusercontent.com/HenryQuan/SendText/master/screenshot.PNG)

# Reference
1. https://github.com/lopspower/AndroidWebServer
2. https://stackoverflow.com/questions/22468026/how-should-i-decode-a-utf-8-string
