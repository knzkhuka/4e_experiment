#include<iostream>
#include<vector>
#include<algorithm>

signed main(){

  std::vector<int> math = {100,56,43,55,78,92,85,64,67};
  std::vector<int> chemistry = {78,92,56,39,32,78,64};

  std::vector<int> connected;
  std::copy(std::begin(math),std::end(math),std::back_inserter(connected));
  std::copy(std::begin(chemistry),std::end(chemistry),std::back_inserter(connected));
  
  std::sort(std::begin(connected),std::end(connected));

  for(auto point:connected)
    std::cout<< point <<std::endl;

}