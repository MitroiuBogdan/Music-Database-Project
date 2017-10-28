/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicsql.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yllub-pc
 */
public class Datasource {
    
    private static final String   DB_NAME="music.db";
    private static final String   CONNECTION_STRING="jdbc:sqlite:D:\\database\\"+
                                                      DB_NAME;
    
    private static final String   TABLE_ALBUMS="albums";
    private static final String   COLUMN_ALBUM_ID="_id";
    private static final String   COLUMN_ALBUM_NAME="name";
    private static final String   COLUMN_ALBUM_ARTIST="artist";
    private static final int      INDEX_ALBUM_ID=1;
    private static final int      INDEX_ALBUM_NAME=2;
    private static final int      INDEX_ALBUM_ARTIST=3;
    
    private static final String   TABLE_ARTISTS="artists";
    private static final String   COLUMN_ARTIST_ID="_id";
    private static final String   COLUMN_ARTIST_NAME="name";
    private static final int      INDEX_ARTIS_ID=1;
    private static final int      INDEX_ARTIST_NAME=2;
    
    private static final String   TABLE_SONGS="songs";
    private static final String   COLUMN_SONG_ID="_id";
    private static final String   COLUMN_SONG_TRACK="track";
    private static final String   COLOMN_SONG_TITLE="title";
    private static final String   COLUMN_SONG_ALBUM="album";
    private static final int      INDEX_SONG_ID=1;
    private static final int      INDEX_SONG_TRACK=2;
    private static final int      INDEX_SONG_TITLE=3;
    private static final int      INDEX_SONG_ALBUM=4;
    
    public static final int      OREDER_BY_NONE=1;
    public static final int      ORDER_BY_ASC=2;
    public static final int      ORDER_BY_DESC=3;  
    
    private static final String  QUERY_SONGS_BY_ARTIST="select "+TABLE_SONGS+"."+COLOMN_SONG_TITLE+" from "+TABLE_SONGS+" inner join "+
                                                        TABLE_ALBUMS+" on "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ID+"="+TABLE_SONGS+"."+COLUMN_SONG_ALBUM+" inner join "+
                                                        TABLE_ARTISTS+" on "+TABLE_ARTISTS+"."+COLUMN_ARTIST_ID+"="+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+
                                                        " where "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+
                                                        "=\"";
    
    
    private Connection conn;
    
    public boolean open(){
     try {
         this.conn=DriverManager.getConnection(CONNECTION_STRING);
         System.out.println("Database has been opened succesfully!");
         return true;
         
     }catch(SQLException e){
         e.printStackTrace();
         return false;
        }
    
     }
    public void close(){
        try{
            if(conn!=null){
                conn.close();
                System.out.println("Database has been closed succesfully!");}
           }catch(SQLException e){
            e.printStackTrace();
            
                }
        }
      
    public List<Artist>  queryArtists(int sortOrder){
 
       StringBuilder sb=new StringBuilder("SELECT * FROM ");
       sb.append(TABLE_ARTISTS);
       if(sortOrder!=OREDER_BY_NONE){
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
        if(sortOrder==ORDER_BY_DESC){
                sb.append("DESC");}
        else{
                sb.append("ASC");
            }
       }
       
       
       
       try(Statement statement=conn.createStatement();
               ResultSet results=statement.executeQuery(sb.toString())){
           
           
         List<Artist> box=new ArrayList<>();
         while(results.next()){
             
             Artist artist=new Artist();
             artist.setId(results.getInt(INDEX_ARTIS_ID));
             artist.setName(results.getString(INDEX_ARTIST_NAME));
             box.add(artist);
             }
         return box;}
       catch(SQLException e){
               e.printStackTrace();
               return null;
         }}
    public List<String>  queryAlbumForArtists(String artistName,int sortOrder){
    
        StringBuilder sb=new StringBuilder("select ");
        sb.append(TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME);
        sb.append(" from "+TABLE_ALBUMS);
        sb.append(" inner join ");
        sb.append(TABLE_ARTISTS+" on ");
        sb.append(TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+"=");
        sb.append(TABLE_ARTISTS+"."+COLUMN_ARTIST_ID);
        sb.append(" where "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME);
        sb.append("= \""+artistName+"\"");
        
        if(sortOrder!=OREDER_BY_NONE){
            sb.append(" order by ");
            sb.append(TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME);
            sb.append(" collate nocase ");
            if(sortOrder==ORDER_BY_DESC){
                sb.append(" desc");}
            else{
                sb.append(" asc");}    }
        
        try(Statement statement=conn.createStatement()){
            List<String> box=new ArrayList<>();
            ResultSet result=statement.executeQuery(sb.toString());
            while(result.next()){
  
            box.add(result.getString(1));
            
            
            }
        
        
             return box;
        }catch(SQLException e){
                e.printStackTrace();
                return null;}
        
        
    }
    public List<String>  queryArtistsForSongs(String artistName){
        StringBuilder sb=new StringBuilder();
        sb.append(QUERY_SONGS_BY_ARTIST+artistName+"\"");
        List<String>box=new ArrayList<>();
        
        try(Statement statement=conn.createStatement()){
            ResultSet result=statement.executeQuery(sb.toString());
            while(result.next()){
                box.add(result.getString(1));
                }
            return box;
        }catch(SQLException e){
            e.printStackTrace();
            return null;}
    
    
    }  
       

       
       }
        
    

    