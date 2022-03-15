package com.example.lab6.socialnetwork.service;

import com.example.lab6.socialnetwork.domain.Friendship;
import com.example.lab6.socialnetwork.domain.Tuple;
import com.example.lab6.socialnetwork.domain.User;
import com.example.lab6.socialnetwork.domain.validators.FriendshipValidator;
import com.example.lab6.socialnetwork.repository.RepositoryFriendship;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class FriendshipService {
    private RepositoryFriendship<Long, Friendship> repo;

    public FriendshipService(RepositoryFriendship<Long, Friendship> repo) {
        this.repo = repo;
    }

    public Friendship addFriendship(Friendship friendship, Map<Long, User> entitiesUsers) {

        FriendshipValidator friendshipValidator=new FriendshipValidator();
        friendshipValidator.validate2(friendship,entitiesUsers);
        return repo.save(friendship);
    }
    public Friendship updateFriendship(Friendship friendship, Map<Long, User> entitiesUsers) {

        FriendshipValidator friendshipValidator=new FriendshipValidator();
        friendshipValidator.validate2(friendship,entitiesUsers);
        Friendship friendship_new=new Friendship(friendship.getId().getLeft(),friendship.getId().getRight());
        friendshipValidator.validate2(friendship_new,entitiesUsers);
        return repo.update(friendship);
    }


    public List<Friendship> getListFriendships(){

        return repo.getListFriendships();

    }

    public Iterable<Friendship> getAll() {
        return repo.findAll();
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
    public Friendship findFriendship(Tuple<Long,Long> id)
    {
        Friendship task=repo.findOne(id);
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
}
