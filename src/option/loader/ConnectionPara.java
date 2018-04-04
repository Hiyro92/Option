/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package option.loader;


public class ConnectionPara{
    public String name   = "";
    public String ip     = "192:168:0:1";
    public int rack      = 0;
    public int sockel    = 0;

    public ConnectionPara() {
    }
    
    public ConnectionPara(String name) {
        this.name    = name;
    }    
    
     public ConnectionPara(String name, String ip, int rack , int sockel) {
         this.name   = name;
         this.ip     = ip;
         this.rack   = rack;
         this.sockel = sockel;
    }

    @Override
    public String toString() {
        return name;
    }
}
