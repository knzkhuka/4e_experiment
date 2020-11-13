#include<iostream>
#include<utility>
#include<map>
#include<vector>
#include<iomanip>

signed main(){
  using namespace std;

  map<string,int> data = {{"Sato",100},{"Matsuno",56},{"Tanaka",43},{"Sakagami",55},{"Kirisima",78},{"Yamaguchi",92},{"Yuba",85},{"Shinoda",64},{"Nagata",67},{"Akiyama",92}};

  auto list_lessthan_n_points = [&data](int n){
    vector<string> list;
    for(auto[name,point]:data){
      if(point<n)list.emplace_back(name);
    }
    return list;
  };
  auto search_from_name = [&data](string name){
    if(!data.count(name))return -1;
    return data[name];
  };

  auto under_60 = list_lessthan_n_points(60);
  cout<<"less than 60 points"<<endl;
  for(auto name:under_60)
    cout<<name<<endl;

  vector<string> names = {"Sato","Tanaka","Nagata","asdf","nagao","aaaaa","ushi","Yuba"};
  cout<<"search point from name"<<endl;
  for(auto name:names){
    auto point = search_from_name(name);
    cout<<setw(6)<<name<<" : ";
    if(point==-1)cout<<"no data"<<endl;
    else cout<<point<<endl;
  }

}