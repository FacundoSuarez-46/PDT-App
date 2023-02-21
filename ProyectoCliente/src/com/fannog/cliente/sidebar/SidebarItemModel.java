package com.fannog.cliente.sidebar;

public class SidebarItemModel {

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String[] getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(String[] subMenu) {
        this.subMenu = subMenu;
    }

    public SidebarItemModel(String menuName, String... subMenu) {
        this.menuName = menuName;
        this.subMenu = subMenu;
    }

    public SidebarItemModel() {
    }

    private String menuName;
    private String subMenu[];
}
