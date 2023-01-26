package Desperatedrosseln.Logic.Cards.Upgrade;

import Desperatedrosseln.Logic.Cards.*;

public class AdminPrivilege extends UpgradeCard {
    //passive UpgradeCard
    public AdminPrivilege(){
        super(true,3);
    }
    @Override
    public String toString() {
        return "AdminPrivilege";
    }
}

