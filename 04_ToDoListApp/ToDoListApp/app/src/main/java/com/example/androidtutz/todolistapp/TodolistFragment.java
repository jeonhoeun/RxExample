package com.example.androidtutz.todolistapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androidtutz.todolistapp.adapter.ToDoListAdapter;
import com.example.androidtutz.todolistapp.adapter.RecyclerTouchListener;
import com.example.androidtutz.todolistapp.data.ToDoListItem;
import com.example.androidtutz.todolistapp.data.ToDoDataManager;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodolistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodolistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodolistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerViewAchievingGoals;
    FloatingActionButton fabAddNewToDo;
    private String taskStatus = "";
    private String dateFront = "";
    private ToDoDataManager toDoDataManager;

    private List<ToDoListItem> goalsList = new ArrayList<>();
    private ToDoListAdapter goalAdapter;

    private View view;
    private TextView searchEditText;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public TodolistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodolistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodolistFragment newInstance(String param1, String param2) {
        TodolistFragment fragment = new TodolistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        toDoDataManager = new ToDoDataManager(getContext());
        toDoDataManager.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_todolist, container, false);
        fabAddNewToDo = view.findViewById(R.id.fab_addNewToDo);
        searchEditText=view.findViewById(R.id.todo_search);

        recyclerViewAchievingGoals = view.findViewById(R.id.recycler_view_achieving_goals);


        if (Integer.parseInt(mParam1) == 0) {


            taskStatus = "Achieving";
            dateFront = "Will be achieved by";

        } else {


            taskStatus = "Achieved";
            dateFront = "Successfully achieved by";
        }


        //set fab
        fabAddNewToDo.setImageResource(R.drawable.ic_add_white);
        fabAddNewToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Integer.parseInt(mParam1) == 0) {

                    Intent add_new_intent = new Intent(getContext(), AddToDoActivity.class);
                    add_new_intent.putExtra("status", "Achieving");
                    startActivity(add_new_intent);


                } else {

                    Intent add_new_intent = new Intent(getContext(), AddToDoActivity.class);
                    add_new_intent.putExtra("status", "Achieved");
                    startActivity(add_new_intent);

                }


            }
        });


        setRecyclerView();
        loadData();

        searchEditText = view.findViewById(R.id.todo_search);

//      아래와 같은 방식은 사용자가 Movie를 친다면 M Mo MOv Movi Movie 를 칠때마다 9번의 search process가 필요한 drawbacks가 있다.
//      아이템이 천여개라면 메모리 이슈도 발생하며 데이터가 remote API로부터 받는방식이라면 앱에 freeze도 발생한다.
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                goalAdapter.getFilter().filter(s);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

/*        대신에 아래와가이 RxBinding을 이용한다.
        주요 내용으로는
        1. skipInitialValue : 초기값에대한 이벤트는 ignore
        2. debounce ( 제한시간동안 방출 안하다가 그시간안에 맨 마지막 이벤트만 방출 주로 UI이벤트에서 활용 )
        3. distinctUntilChanged ( 변화가 있을때만 방출한다. distinct와는 다르게 중복도 방출된다.)
*/

compositeDisposable.add(
        RxTextView.textChangeEvents(searchEditText)
                .skipInitialValue()
                .debounce(300,TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TextViewTextChangeEvent>() {
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        goalAdapter.getFilter().filter(textViewTextChangeEvent.getText());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
);



        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setRecyclerView() {


        goalAdapter = new ToDoListAdapter(goalsList, 0, TodolistFragment.this);

        recyclerViewAchievingGoals.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAchievingGoals.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAchievingGoals.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerViewAchievingGoals.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewAchievingGoals, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                ToDoListItem goal = goalsList.get(position);
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("goal_related_data", MODE_PRIVATE).edit();
                editor.putString("goal_id", Integer.toString(goal.getToDoListItemId()));
                editor.putString("status", "0");
                editor.commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerViewAchievingGoals.setAdapter(goalAdapter);


    }

    private void loadData() {

        Observable<ToDoListItem> observable = Observable.fromArray(toDoDataManager.getAllToDoListItem_list().toArray(new ToDoListItem[0]));
        compositeDisposable.add(
                observable.subscribeOn(Schedulers.io())
                        .filter(new Predicate<ToDoListItem>() {
                            @Override
                            public boolean test(ToDoListItem toDoListItem) throws Exception {
                                return toDoListItem.getToDoListItemStatus().equals(taskStatus);
                            }
                        })
                        .map(new Function<ToDoListItem, ToDoListItem>() {

                            @Override
                            public ToDoListItem apply(ToDoListItem toDoListItem) throws Exception {
                                toDoListItem.setToDoListItemDescription(dateFront+ " "+toDoListItem.getToDoListItemPlanedAchievDate());
                                return toDoListItem;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<ToDoListItem>() {
                    @Override
                    public void onNext(ToDoListItem toDoListItem) {
                        goalsList.add(toDoListItem);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        goalAdapter.notifyDataSetChanged();
                    }
                })

        );



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void markAsAchieved(int position) {


        if (Integer.parseInt(mParam1) == 0) {


            ToDoListItem goal = goalsList.get(position);
            goal.setToDoListItemStatus("Achieved");
            toDoDataManager.updateToDoListItem(goal);
            goalsList.remove(position);

            goalAdapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), "Marked as achieved", Toast.LENGTH_LONG).show();

        } else {


            ToDoListItem goal = goalsList.get(position);
            goal.setToDoListItemStatus("Achieving");
            toDoDataManager.updateToDoListItem(goal);
            goalsList.remove(position);

            goalAdapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), "Marked as not achieved", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


            if (recyclerViewAchievingGoals != null) {
                goalsList.clear();
                setRecyclerView();
                loadData();

            }

        }
    }

    public void viewListWithButtons(int position) {


        ToDoListItem goal = goalsList.get(position);
        goal.setToDoListItemBackgroundColor("1");
        goalAdapter.notifyItemChanged(position);


    }

    public void closeButtonsRow(int position) {


        ToDoListItem goal = goalsList.get(position);
        goal.setToDoListItemBackgroundColor("0");
        goalAdapter.notifyItemChanged(position);


    }

    public void deleteRow(int position) {

        final int selctedPosition = position;

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_demo);
        dialog.setTitle("");

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialog_ok);
        dialogButtonOk.setText("Delete!");

        TextView dialogDesc = (TextView) dialog.findViewById(R.id.dialog_message);

        dialogDesc.setText("Are you sure?");

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToDoListItem goal = goalsList.get(selctedPosition);
                toDoDataManager.deleteToDoListItem(goal);
                goalsList.remove(selctedPosition);
                dialog.dismiss();
                goalAdapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();

    }

    public void viewUpdateFragment(int position) {

        ToDoListItem goal = goalsList.get(position);
        String content = goal.getToDoListItemName();

        FragmentManager oFragmentManager = getActivity().getSupportFragmentManager();
        UpdateToDoFragment updateTasksFragment = UpdateToDoFragment.newInstance(content, 0, TodolistFragment.this, goal);
        updateTasksFragment.show(oFragmentManager, "Sample Fragment");

    }



}
