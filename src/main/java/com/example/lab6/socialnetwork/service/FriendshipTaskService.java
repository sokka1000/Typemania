package com.example.lab6.socialnetwork.service;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.FriendshipDTO;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.FriendshipValidator;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;
import com.example.lab6.socialnetwork.repository.paging.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;



public class FriendshipTaskService {
    private PagingRepositoryFriendship<Long, Friendship> repo;


    public FriendshipTaskService(PagingRepositoryFriendship<Long, Friendship> repo) {
        this.repo = repo;
    }

    public <T> Iterable <T> filter(Iterable <T> list, Predicate<T> cond)
    {
        List<T> rez=new ArrayList<>();
        list.forEach((T x)->{if (cond.test(x)) rez.add(x);});
        return rez;
    }

    public Iterable<Friendship> getAll(){
        return repo.findAll();
    }

    private int page = 0;
    private int size = 4;

    public Friendship addFriendship(Friendship friendship) {

        return repo.save(friendship);
    }

    public List<Friendship> getListFriendships(){

        return repo.getListFriendships();

    }

    public Friendship findOne(Tuple<Long,Long> id)
    {
        return repo.findOne(id);
    }

    public Friendship deleteFriendship(Tuple<Long,Long> id)
    {
        Friendship task=repo.delete(id);
        return task;
    }









    public int numberOfCommunities(Iterable<User> users)
    {
        Iterable <Friendship> friendships= repo.findAll();
        Vector<Long> v=new Vector<>();
        Vector<Long> f=new Vector<>();
        Vector<Long> query=new Vector<>();



        for(User user: users)
        {
            v.add(user.getId());
            f.add(0L);
            query.add(0L);

        }
        int n=v.size(),contor=0;
        int[] frec=new int[v.size()];
        for(int i=0;i<n;i++)
            frec[i]=0;

        for(int i=0;i<n;i++)
            if(frec[i]==0)
            {
                contor++;
                frec[i]=1;
                int st=0,dr=0;

                query.add(0,v.get(i));
                while(st<=dr)
                {
                    Long k=query.get(st);
                    for(int j=0;j<n;j++)
                        if(frec[j]==0 && null!=repo.findOne(new Tuple<>(v.get(j),k)))
                        {
                            dr++;
                            frec[j]=1;
                            query.add(dr,v.get(j));
                        }
                    st++;

                }


            }




        return contor;
    }

    public long[] sociableCommunity(Iterable<User> users)
    {
        Iterable <Friendship> friendships= repo.findAll();
        Vector<Long> v=new Vector<>();
        Vector<Long> f=new Vector<>();
        Vector<Long> query=new Vector<>();



        for(User user: users)
        {
            v.add(user.getId());
            f.add(0L);
            query.add(0L);

        }
        int n=v.size(),contor=0,nr=0,unic=0,max=0;
        int[] frec=new int[v.size()];
        long[] friends=new long[v.size()+1];
        long[] components=new long[v.size()+1];

        for(int i=0;i<n;i++)
            frec[i]=0;

        for(int i=0;i<n;i++)
            if(frec[i]==0)
            {
                contor++;

                frec[i]=1;
                int st=0,dr=0;

                for(int g=0;g<=n;g++)
                    friends[g]=0;

                unic=1;
                nr=0;
                friends[unic]=v.get(i);



                query.add(0,v.get(i));
                while(st<=dr)
                {
                    Long k=query.get(st);
                    for(int j=0;j<n;j++)
                    {
                        if(null!=repo.findOne(new Tuple<>(v.get(j),k)))
                        {
                            nr++;
                            if(frec[j]==0)
                            {
                                unic++;
                                friends[unic]=v.get(j);
                            }

                        }

                        if(frec[j]==0 && null!=repo.findOne(new Tuple<>(v.get(j),k)))
                        {
                            dr++;
                            frec[j]=1;
                            query.add(dr,v.get(j));
                        }


                    }


                    st++;

                }


                if(nr>max)
                {

                    max=nr;
                    for(int g=1;g<=n;g++)
                        components[g]=friends[g];
                    components[0]=unic;

                }





            }



        return components;

    }



    public List<User> filterUsersName(String s) {
        return null;
    }

    public List<User> getUserFriends(Long id, List<User> friendsForSearchedUser) {
        List<User> commonFriendsForSearchedUser = new ArrayList<>();
        Iterable<Friendship> friendshipIterable = this.getAll();
        Map<Tuple<Long, Long>, Friendship> friendshipMap = new HashMap<>();
        for (Friendship friendship : friendshipIterable)
        {
            friendshipMap.put(new Tuple<>(friendship.getID1(), friendship.getID2()),friendship);

        }
        for(User user : friendsForSearchedUser)
        {
            if(friendshipMap.containsKey(new Tuple<>(user.getId(),id)) || friendshipMap.containsKey(new Tuple<>(id,user.getId())) )
                commonFriendsForSearchedUser.add(user);
        }

        return commonFriendsForSearchedUser;
    }

    public Map<String, String> allFriends(String username)
    {
        return repo.allFriends(username);
    }

    public  Map<String, LocalDate> friendsTimePeriod(String username, LocalDate date1, LocalDate date2){
        Map<String, String> friends = allFriends(username);
        Map<String, LocalDate> searchedFriends = new HashMap<>();
        for(String usernameFriend: friends.keySet()){
            LocalDate friendshipDate = LocalDate.parse(friends.get(usernameFriend).substring(0, 10));
            if((friendshipDate.isBefore(date2) && friendshipDate.isAfter(date1)) || friendshipDate.isEqual(date1) || friendshipDate.isEqual(date2))
            {
                searchedFriends.put(usernameFriend, friendshipDate);
            }
        }
        return searchedFriends;
    }
}
