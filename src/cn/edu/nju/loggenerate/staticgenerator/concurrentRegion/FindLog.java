package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

import java.util.*;

public class FindLog {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FindLog log = new FindLog();
		List<List<String>> test = new ArrayList<List<String>>();
		List<String> first = new ArrayList<String>();
		first.add("a");
		first.add("b");
		first.add("c");
		first.add("d");
		List<String> second = new ArrayList<String>();
		second.add("a");
		second.add("b");

		List<String> third = new ArrayList<String>();
		third.add("c");
		third.add("d");

		List<String> fourth = new ArrayList<String>();
		fourth.add("x");
		fourth.add("y");
		fourth.add("z");
		List<String> fifth = new ArrayList<String>();
		fifth.add("m");
		fifth.add("n");
		fifth.add("q");

		test.add(first);
		test.add(second);
		test.add(third);
		test.add(fourth);
		test.add(fifth);

		log.findLongList(test);
		System.out.println(log.preList.size());
		for (List<String> str : log.getRelateList()) {
			for (String str1 : str)
				System.out.print(str1);
			System.out.println();
		}
		for (List<String> str : log.getUnrelateList()) {
			for (String str1 : str)
				System.out.print(str1);
			System.out.println();
		}
	}

	public List<List<String>> totalList;

	public PermutationGenerator per;

	public Set<List<String>> preList;
	public Set<Integer> preSet;

	public List<List<String>> relateList;
	public List<List<String>> unrelateList;
	public FindLog() {
		totalList = new ArrayList<List<String>>();

		preList = new HashSet<List<String>>();
		preSet = new HashSet<Integer>();
		relateList=new ArrayList<List<String>>();
		unrelateList=new ArrayList<List<String>>();
	}

	public List<List<String>> getRelateList() {

		return relateList;
	}
	public List<List<String>> getUnrelateList()
	{
		return unrelateList;
	}

	public boolean findLongList(List<List<String>> total) {
		this.totalList = total;
		per = new PermutationGenerator(total.size());

		Set<Integer> indexSet = new HashSet<Integer>();
		Set<List<String>> longList = new HashSet<List<String>>();
		
		while (per.hasMore()) {

			int[] indices = per.getNext();
			Map<Integer, Integer> indiceMap = new HashMap<Integer, Integer>();
			for (int i = 0; i < indices.length; i++) {
				indiceMap.put(i, indices[i]);
				
			}
			
			// preList=longList;
			longList = new HashSet<List<String>>();
			indexSet = new HashSet<Integer>();
			int start = 0;
			int end = total.size() - 1;
			while (start < end) {
				List<String> start1 = total.get(indiceMap.get(start));
				List<String> end1 = total.get(indiceMap.get(end));
				if (check(start1, end1)) {
					// continue;
				} else

				{
					if (indexSet.contains(indiceMap.get(start))
							|| indexSet.contains(indiceMap.get(end))) {
						if (indexSet.contains(indiceMap.get(start))) {
							int flag = 0;
							for (List<String> str : longList) {
								if (check(str, end1)) {
									flag = 1;
									break;
								}
							}
							if (flag == 0) {
								indexSet.add(indiceMap.get(end));
								longList.add(end1);
							}
						} else {
							int flag = 0;
							for (List<String> str : longList) {
								if (check(str, start1)) {
									flag = 1;
									break;
								}
							}
							if (flag == 0) {
								indexSet.add(indiceMap.get(start));
								longList.add(start1);
							}
						}
					} else {
						int flag = 0;
						for (List<String> str : longList) {
							if (check(str, start1)) {
								flag = 1;
								break;
							}
						}
						if (flag == 0) {
							indexSet.add(indiceMap.get(start));
							longList.add(start1);
						}
						flag = 0;
						for (List<String> str : longList) {
							if (check(str, end1)) {
								flag = 1;
								break;
							}
						}
						if (flag == 0) {
							indexSet.add(indiceMap.get(end));
							longList.add(end1);
						}
					}
					/*
					 * longList.add(start1); longList.add(end1);
					 */
				}
				end--;
				if (start == end) {
					start++;
					end = total.size() - 1;
				}
			}

			if (longList.size() > preList.size()
					|| (longList.size() == preList.size() && elemSize(longList) > elemSize(preList))) {
				preList = longList;
				preSet = indexSet;
			}

		}
		for(int in=0;in<total.size();in++)
		{
			if(preSet.contains(in))
			{
				unrelateList.add(total.get(in));
			}else
			{
				relateList.add(total.get(in));
			}
		}
		return true;
	}

	public int elemSize(Set<List<String>> test) {
		int result = 0;
		for (List<String> str : test) {
			result += str.size();
		}
		return result;
	}

	public boolean check(List<String> list1, List<String> list2)

	{
		Set<String> set1 = new HashSet<String>();
		set1.addAll(list1);
		Set<String> set2 = new HashSet<String>();
		set2.addAll(list2);
		boolean bool = set1.removeAll(set2);
		// 为真代表为有交集，
		return bool;
	}
}
