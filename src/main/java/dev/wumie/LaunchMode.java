package dev.wumie;

public enum LaunchMode {
    Forward("forward"),
    Server("server"),
    Client("client"),
    ForwardServer("forward_s"),
    ForwardClient("forward_C");

    public final String modeName;

    LaunchMode(String modeName) {
        this.modeName = modeName;
    }

    public String getTitle() {
        return  ((modeName.charAt(0)) + "").toUpperCase()+modeName.substring(1);
    }
}
