/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicsql.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import jdk.nashorn.internal.runtime.regexp.joni.constants.CCVALTYPE;

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
    
    private static final String  QUERY_SONGS_BY_ARTIST="select "+TABLE_SONGS+"."+COLUMN_SONG_TRACK
                                                        +" from "+TABLE_SONGS+" inner join "+
                                                        TABLE_ALBUMS+" on "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ID+"="+TABLE_SONGS+"."+COLUMN_SONG_ALBUM+" inner join "+
                                                        TABLE_ARTISTS+" on "+TABLE_ARTISTS+"."+COLUMN_ARTIST_ID+"="+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+
                                                        " where "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+
                                                        "=\"";
    private static final String TABLE_SONG_VIEW="song_list2";
    private static final String  CREATR_VIEW_SONGS="create view if not exists "+TABLE_SONG_VIEW+" as select "+TABLE_SONGS+"."+COLOMN_SONG_TITLE+" from "+TABLE_SONGS;
    private static final String  QUERY_SONG_INFO=" select "+TABLE_ARTISTS+"."+COLUMN_ARTIST_NAME+" , "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTIST+" from "+
                                                            TABLE_ARTISTS+" inner join "+TABLE_ALBUMS+" on "+TABLE_ARTISTS+"."+COLUMN_ARTIST_ID+"="+TABLE_ALBUMS+"."+
                                                            COLUMN_ALBUM_ARTIST+" inner join "+TABLE_SONGS+" on "+TABLE_SONGS+"."+COLUMN_SONG_ALBUM+"="+TABLE_ALBUMS+"."
                                                            +COLUMN_ALBUM_ID+" where "+TABLE_SONGS+"."+COLOMN_SONG_TITLE+" =?";
    private static final String INSERT_ARTIST="insert into "+TABLE_ARTISTS+" ("+COLUMN_ARTIST_NAME+") values (?)";
    private static final String INSERT_ALBUMS="insert into "+TABLE_ALBUMS+" ("+COLUMN_ALBUM_NAME+","+COLUMN_ALBUM_ARTIST+") values (?, ?)";
    private static final String INSERT_SONGS="insert  into "+TABLE_SONGS+" ("+COLUMN_SONG_TRACK+","+COLOMN_SONG_TITLE+","+COLUMN_SONG_ALBUM+
                                                            ") values(?,?,?)";
    
    private static final String QUERY_ARTISTS="select "+COLUMN_ARTIST_NAME+" from "+TABLE_ARTISTS+" where "+COLUMN_ARTIST_NAME+"= ?";
    private static final String QUERY_ALBUMS="select  "+COLUMN_ALBUM_NAME+" from "+TABLE_ALBUMS+" where "+COLUMN_ALBUM_NAME+"= ?";                                                  
    
    
    
    
    private Connection conn;
    private PreparedStatement querySongInfo;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertintoSongs;
    
    private PreparedStatement query_Artists;
    private PreparedStatement query_Albums;
    
    public boolean open(){
     try {
         
         this.conn=DriverManager.getConnection(CONNECTION_STRING);
         this.querySongInfo=conn.prepareStatement(QUERY_SONG_INFO);
         this.insertIntoArtists=conn.prepareStatement(INSERT_ARTIST,Statement.RETURN_GENERATED_KEYS);
         this.insertIntoAlbums=conn.prepareStatement(INSERT_ALBUMS,Statement.RETURN_GENERATED_KEYS);
         this.insertintoSongs=conn.prepareStatement(INSERT_SONGS);
         this.query_Albums=conn.prepareStatement(QUERY_ALBUMS);
         this.query_Artists=conn.prepareStatement(QUERY_ARTISTS);
         
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
                querySongInfo.close();
                insertintoSongs.close();
                insertIntoAlbums.close();
                insertIntoArtists.close();
                query_Artists.close();
                query_Albums.close();
                
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
    public void querySongsMetadate(){
        String sb="select * from "+TABLE_SONGS;
        try(Statement statement=conn.createStatement();
            ResultSet result=statement.executeQuery(sb.toString())){
            
            ResultSetMetaData meta=result.getMetaData();
            int numColums=meta.getColumnCount();
            
            for(int i=1;i<numColums+1;i++){
                System.out.println(i+" "+meta.getColumnName(i).toString());}
            
            
        
        }catch(SQLException e){
            e.printStackTrace();}
    }   
    public int getCount(String table){
        StringBuilder sb=new StringBuilder();
        sb.append("select count(*) as count from "+table);
        
        try(Statement statement=conn.createStatement();
               ResultSet result=statement.executeQuery(sb.toString())){
              
               int tableCont=result.getInt("count");
            
               return tableCont;
        }catch(SQLException e){ 
                e.printStackTrace();
                return -1;}
    
    
    }
    public  boolean createViewForSongs(){
        
        try(Statement statement=conn.createStatement()){
            statement.execute(CREATR_VIEW_SONGS);
        
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;}
    
    }
    public  void queryViewForSongs(){
        StringBuilder sb=new StringBuilder();
        sb.append("select * from "+TABLE_SONG_VIEW);
        try(Statement statement=conn.createStatement();ResultSet result=statement.executeQuery(sb.toString())){
                while(result.next()){
                System.out.println(result.getString(1));}
        }catch(SQLException e){
                e.printStackTrace();}
    }
    public  void getSongInfo(String song){
     
        
        try{
            querySongInfo.setString(1,song);
            ResultSet result=querySongInfo.executeQuery();
            
            while(result.next()){
                System.out.println(result.getString(1)+" "+result.getString(2));}
            
        }catch(SQLException e){
            System.out.println("That song dosent exist");}
        
        
    }
    private int  insertArtists(String name) throws SQLException{
        query_Artists.setString(1, name);   //query Artists and search for name;
        ResultSet results=query_Artists.executeQuery();
        if(results.next()){
            return results.getInt(1);}
        else{
            // Insert the artist;
            insertIntoArtists.setString(1, name);
            int affectedRows=insertIntoArtists.executeUpdate(); //Update rows and 
                                                                //return nr of rows updated;
            
            if(affectedRows!=1){
                throw new SQLException("Couldn't insert artist!!");}
            
            ResultSet generatedKeys=insertIntoArtists.getGeneratedKeys();
            if(generatedKeys.next()){
                return generatedKeys.getInt(1);}
            else{
                throw new SQLException("Coudnt get _id for artists");}
        }
    
   }



}
 

    

    