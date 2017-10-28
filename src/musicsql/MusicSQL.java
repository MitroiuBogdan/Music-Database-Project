/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicsql;

import java.util.List;
import musicsql.model.Artist;
import musicsql.model.Datasource;

/**
 *
 * @author Yllub-pc
 */
public class MusicSQL {

    
    public static void main(String[] args) {
        
        Datasource dataSource=new Datasource();
        if(!dataSource.open()){
            System.out.println("Cant Open Datasource!");
            return;}
        
        
        /*List<Artist>artists=dataSource.queryArtists(1);
        if(artists==null){
            System.out.println("No artists!");}
        else{
        artists.stream()
                .map((x)->x.getId()+" "+x.getName())
                .forEach(System.out::println);}
        */
        List<String>album=dataSource.queryAlbumForArtists("Elf",Datasource.ORDER_BY_ASC);
        List<String>songs=dataSource.queryArtistsForSongs("Elf");
            songs.forEach(System.out::println);
        //album.forEach(System.out::println);
        
        dataSource.close();
        
    }
    
}
