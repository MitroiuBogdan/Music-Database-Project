/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicsql;

import java.sql.SQLException;
import java.util.List;
import musicsql.model.Artist;
import musicsql.model.Datasource;

/**
 *
 * @author Yllub-pc
 */
public class MusicSQL {

    
    public static void main(String[] args) throws SQLException {
        
        Datasource dataSource=new Datasource();
        if(!dataSource.open()){
            System.out.println("Cant Open Datasource!");
            return;}
        
      //  dataSource.querySongsMetadate();
        //System.out.println(dataSource.getCount("songs"));
  
     dataSource.insertSongs("Haleluia","NikiMinaj","The PinkPrint",4);
     
     // dataSource.getSongInfo("Shock Wave");
        dataSource.close();
        
    }
    
}
