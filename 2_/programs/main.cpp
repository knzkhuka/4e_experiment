#include<iostream>
#include<vector>
#include<initializer_list>
#include<algorithm>
#include<stack>
#include<map>
#include<iomanip>

class solver{
  struct node{
    int Number;
    std::string Name;
    std::vector<int> Scoers;

    node() = default;
    node(int num,std::string name,std::initializer_list<int> init)
      :Number(num),Name(name),Scoers(init){}

    friend std::ostream& operator << (std::ostream& os,const node& elm){
      os << std::setw(2) << elm.Number;
      os << std::setw(10) << elm.Name;
      for(const auto& score : elm.Scoers)
        os << std::setw(4) << score;
      return os;
    }

    const int average()const{
      int sum = 0;
      int n = std::size(Scoers);
      for(int score:Scoers)
        sum += score;
      return sum/n;
    }
  };
  template<class Cmp>
  inline void sort_data(Cmp cmp){
    std::sort(std::begin(data),std::end(data),cmp);
  }

public:
  solver() = default;
  void add(int num,std::string name,std::initializer_list<int> init){
    data.emplace_back(num,name,init);
  }
  void print_number(){
    sort_data([](const node& a,const node& b){return a.Number < b.Number;});
    for(auto elm:data){
      std::cout << elm << std::endl;
    }
    std::cout << std::endl;
  }
  void print_rev_number(){
    sort_data([](const node& a,const node& b){return a.Number < b.Number;});
    std::stack<node> st;
    for(auto elm:data)st.emplace(elm);
    while(!st.empty()){
      std::cout << st.top() << std::endl;
      st.pop();
    }
    std::cout << std::endl;
  }
  void print_name(){
    std::map<std::string,node> mp;
    for(auto elm:data)mp[elm.Name]=elm;
    for(auto [key,elm]:mp){
      std::cout << elm << std::endl;
    }
    std::cout << std::endl;
  }
  void print_with_average(){
    sort_data([](const node& a,const node& b){return a.Number < b.Number;});
    for(auto elm:data){
      std::cout << elm << " " << elm.average() << std::endl;
    }
    std::cout << std::endl;
  }
  void print_sorted_average(){
    sort_data([](const node& a,const node& b){return a.average() < b.average();});
    for(auto elm:data){
      std::cout << elm << " " << elm.average() << std::endl;
    }
    std::cout << std::endl;
  }
  void print_initial(std::string ini){
    auto cmp = [](const node& a,const node& b){return a.Name < b.Name;};
    sort_data(cmp);
    auto first = std::lower_bound(std::begin(data),std::end(data),node(-1,ini,{}),cmp);
    ini.back()++;
    auto last = std::lower_bound(std::begin(data),std::end(data),node(-1,ini,{}),cmp);
    for(auto itr=first;itr!=last;++itr){
      std::cout << *itr <<std::endl;
    }
    std::cout << std::endl;
  }
private:
  std::vector<node> data;
};

signed main(){
  solver solve;

  solve.add(1,"Michael",{75,86,89,31});
  solve.add(2,"Emma",{74,63,70,48});
  solve.add(3,"Hannah",{73,45,82,31});
  solve.add(4,"Emily",{40,30,49,48});
  solve.add(5,"Daniel",{46,59,47,70});

  solve.print_number();
  solve.print_rev_number();
  solve.print_name();
  solve.print_with_average();
  solve.print_sorted_average();
  solve.print_initial("E");
}